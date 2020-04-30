package org.librarysimplified.http.vanilla.internal

import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import one.irradia.mime.api.MIMEType
import one.irradia.mime.vanilla.MIMEParser
import org.librarysimplified.http.api.LSHTTPProblemReport
import org.librarysimplified.http.api.LSHTTPProblemReportParserType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.AllowRedirects
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.AllowRedirects.ALLOW_REDIRECTS
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.AllowRedirects.DISALLOW_REDIRECTS
import org.librarysimplified.http.api.LSHTTPRequestType
import org.librarysimplified.http.api.LSHTTPResponseStatus
import java.io.ByteArrayInputStream

class LSHTTPRequest(
  private val client: LSHTTPClient,
  private val allowRedirects: AllowRedirects,
  private val request: Request
) : LSHTTPRequestType {

  override fun execute(): LSHTTPResponse {
    try {
      val call =
        when (this.allowRedirects) {
          ALLOW_REDIRECTS ->
            this.client.client.newCall(this.request)
          DISALLOW_REDIRECTS ->
            this.client.clientWithoutRedirects.newCall(this.request)
        }

      val response = call.execute()
      val responseCode = response.code
      val responseMessage = response.message

      val responseContentType =
        this.parseResponseContentType(response)
      val responseLength =
        response.body?.contentLength()

      this.client.logger.debug(
        "[{}] <- {} {} ({} octets, {})",
        this.request.url,
        responseCode,
        responseMessage,
        responseLength,
        responseContentType
      )

      val responseBody = response.body
      val problemReport =
        this.parseProblemReportIfNecessary(
          responseContentType = responseContentType,
          responseBody = responseBody
        )

      val responseStream =
        if (problemReport == null) {
          responseBody?.byteStream()
        } else {
          null
        }

      val adjustedStatus =
        if (problemReport?.status != null) {
          val status = problemReport.status!!
          this.client.logger.debug(
            "[{}]: problem report changed status {} -> {}",
            this.request.url,
            responseCode,
            status
          )
          status
        } else {
          responseCode
        }

      return if (adjustedStatus >= 400) {
        LSHTTPResponse(
          this,
          response = response,
          status = LSHTTPResponseStatus.Responded.Error(
            status = adjustedStatus,
            originalStatus = responseCode,
            contentType = responseContentType,
            contentLength = responseLength,
            problemReport = problemReport,
            message = responseMessage,
            bodyStream = responseStream
          )
        )
      } else {
        LSHTTPResponse(
          this,
          response = response,
          status = LSHTTPResponseStatus.Responded.OK(
            status = adjustedStatus,
            originalStatus = responseCode,
            contentType = responseContentType,
            contentLength = responseLength,
            problemReport = problemReport,
            message = responseMessage,
            bodyStream = responseStream
          )
        )
      }
    } catch (e: Exception) {
      this.client.logger.error(
        "[{}]: request failed: ",
        this.request.url,
        e
      )
      return LSHTTPResponse(
        request = this,
        response = null,
        status = LSHTTPResponseStatus.Failed(e)
      )
    }
  }

  private fun parseProblemReportIfNecessary(
    responseContentType: MIMEType,
    responseBody: ResponseBody?
  ): LSHTTPProblemReport? {
    val responseType = responseContentType.fullType
    val problemType = LSHTTPMimeTypes.problemReport.fullType
    return if (responseType == problemType && responseBody != null) {
      try {
        this.client.problemReportParsers.createParser(
          uri = this.request.url.toString(),
          stream = responseBody.byteStream()
        ).use(LSHTTPProblemReportParserType::execute)
      } catch (e: Exception) {
        null
      }
    } else {
      null
    }
  }

  private fun parseResponseContentType(
    response: Response
  ): MIMEType {
    val contentType =
      response.header("content-type") ?: return LSHTTPMimeTypes.octetStream

    return try {
      MIMEParser.parseRaisingException(contentType)
    } catch (e: Exception) {
      this.client.logger.error(
        "[{}]: could not parse content type: {}: ", this.request.url, contentType, e
      )
      LSHTTPMimeTypes.octetStream
    }
  }
}
