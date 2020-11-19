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
    for (i in 0 until request.headers.size) {
      val name = request.headers.name(i)
      val values = request.headers.values(name)
      for (value in values) {
        this.logger.trace("[{}] {}: {}", request.url, name, value)
      }
    }
    return chain.proceed(request)
  }
}
