package org.librarysimplified.http.bearer_token.internal

import okhttp3.Interceptor
import okhttp3.Response
import org.librarysimplified.http.bearer_token.LSHTTPBearerTokenInterceptors.Companion.bearerTokenContentType
import org.slf4j.LoggerFactory

class LSHTTPBearerTokenInterceptor : Interceptor {

  private val logger =
    LoggerFactory.getLogger(LSHTTPBearerTokenInterceptor::class.java)

  override fun intercept(
    chain: Interceptor.Chain
  ): Response {
    val originalRequest = chain.request()
    val response = chain.proceed(originalRequest)
    if (response.header("content-type") == bearerTokenContentType) {
      this.logger.debug("encountered a bearer token for {}", originalRequest.url)
      val body = response.body ?: return this.errorEmptyBody(response)
      return try {
        val token =
          body.byteStream()
            .use(LSSimplifiedBearerTokenJSON::deserializeFromStream)

        val newRequest =
          originalRequest
            .newBuilder()
            .header("Authorization", "Bearer ${token.accessToken}")
            .url(token.location.toString())
            .build()

        this.logger.debug("sending a new request to {}", newRequest.url)
        chain.proceed(newRequest)
      } catch (e: Exception) {
        this.errorBadBearerToken(response, e)
      }
    }

    return response
  }

  private fun errorBadBearerToken(
    response: Response,
    exception: Exception
  ): Response {
    this.logger.error("could not parse bearer token: ", exception)
    return response.newBuilder()
      .code(499)
      .message("Bearer token interceptor (LSHTTPBearerTokenInterceptor) parser failed: ${exception.message}")
      .build()
  }

  private fun errorEmptyBody(response: Response): Response {
    this.logger.warn("received empty body from server")
    return response.newBuilder()
      .code(499)
      .message("Bearer token interceptor (LSHTTPBearerTokenInterceptor) received an empty body from the server!")
      .build()
  }
}
