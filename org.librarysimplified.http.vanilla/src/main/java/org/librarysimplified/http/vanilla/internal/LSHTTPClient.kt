package org.librarysimplified.http.vanilla.internal

import okhttp3.OkHttpClient
import okhttp3.Request
import org.librarysimplified.http.api.LSHTTPClientConfiguration
import org.librarysimplified.http.api.LSHTTPClientType
import org.librarysimplified.http.api.LSHTTPProblemReportParserFactoryType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType
import org.librarysimplified.http.vanilla.BuildConfig
import org.slf4j.Logger
import java.net.URI

class LSHTTPClient(
  val logger: Logger,
  val configuration: LSHTTPClientConfiguration,
  val problemReportParsers: LSHTTPProblemReportParserFactoryType,
  val client: OkHttpClient,
  val clientWithoutRedirects: OkHttpClient
) : LSHTTPClientType {

  override fun newRequest(url: URI): LSHTTPRequestBuilderType {
    val builder = Request.Builder().url(url.toString())
    val requestBuilder = LSHTTPRequestBuilder(this, builder)
    requestBuilder.addHeader("User-Agent", this.userAgent())
    return requestBuilder
  }

  private fun userAgent(): String {
    return "${configuration.applicationName}/${configuration.applicationVersion} (Simplified-Android-HTTP ${BuildConfig.HTTP_VERSION_NAME})"
  }
}
