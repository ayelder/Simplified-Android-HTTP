package org.librarysimplified.http.vanilla.internal

import okhttp3.Interceptor
import okhttp3.Response
import org.slf4j.Logger

/**
 * A trivial logging interceptor.
 */

class LSHTTPInterceptor(
  private val logger: Logger
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    this.logger.debug("[{}] -> {}", request.url, request.method)
    return chain.proceed(request)
  }
}
