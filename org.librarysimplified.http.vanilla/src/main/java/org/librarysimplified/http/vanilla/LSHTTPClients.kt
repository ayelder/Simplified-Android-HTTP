package org.librarysimplified.http.vanilla

import android.content.Context
import okhttp3.OkHttpClient
import org.librarysimplified.http.api.LSHTTPClientConfiguration
import org.librarysimplified.http.api.LSHTTPClientProviderType
import org.librarysimplified.http.api.LSHTTPClientType
import org.librarysimplified.http.api.LSHTTPProblemReportParserFactoryType
import org.librarysimplified.http.vanilla.extensions.LSHTTPInterceptorFactoryType
import org.librarysimplified.http.vanilla.internal.LSHTTPClient
import org.librarysimplified.http.vanilla.internal.LSHTTPInterceptor
import org.slf4j.LoggerFactory
import java.util.ServiceLoader
import java.util.concurrent.TimeUnit

/**
 * A provider of okhttp clients.
 */

class LSHTTPClients(
  private val problemReportParsers: LSHTTPProblemReportParserFactoryType,
  private val interceptors: List<LSHTTPInterceptorFactoryType>
) : LSHTTPClientProviderType {

  constructor() : this(
    problemReportParsers = LSHTTPProblemReportParsers(),
    interceptors = ServiceLoader.load(LSHTTPInterceptorFactoryType::class.java).toList()
  )

  private val logger =
    LoggerFactory.getLogger(LSHTTPClients::class.java)

  override fun create(
    context: Context,
    configuration: LSHTTPClientConfiguration
  ): LSHTTPClientType {
    val defaultClientBuilder =
      OkHttpClient.Builder()
        .addInterceptor(LSHTTPInterceptor(this.logger))
        .callTimeout(1L, TimeUnit.MINUTES)
        .connectTimeout(1L, TimeUnit.MINUTES)
        .followSslRedirects(false)

    val defaultClientWithoutRedirectsBuilder =
      OkHttpClient.Builder()
        .addInterceptor(LSHTTPInterceptor(this.logger))
        .callTimeout(1L, TimeUnit.MINUTES)
        .connectTimeout(1L, TimeUnit.MINUTES)
        .followRedirects(false)
        .followSslRedirects(false)

    this.logger.debug("{} available client interceptor extensions", this.interceptors.size)
    for (index in 0 until this.interceptors.size) {
      val interceptorFactory = this.interceptors[index]
      this.logger.debug("[{}] interceptor {} {}", index, interceptorFactory.name, interceptorFactory.version)
      val interceptor = interceptorFactory.createInterceptor(context)
      defaultClientBuilder.addInterceptor(interceptor)
      defaultClientWithoutRedirectsBuilder.addInterceptor(interceptor)
    }

    return LSHTTPClient(
      logger = this.logger,
      problemReportParsers = this.problemReportParsers,
      configuration = configuration,
      client = defaultClientBuilder.build(),
      clientWithoutRedirects = defaultClientWithoutRedirectsBuilder.build()
    )
  }
}
