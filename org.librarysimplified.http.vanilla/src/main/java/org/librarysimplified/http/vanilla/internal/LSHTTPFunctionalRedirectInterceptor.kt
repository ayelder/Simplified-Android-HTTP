package org.librarysimplified.http.vanilla.internal

import okhttp3.Interceptor
import okhttp3.Response
import org.librarysimplified.http.api.LSHTTPRequestProperties

class LSHTTPFunctionalRedirectInterceptor(
  private val modifier: (LSHTTPRequestProperties) -> LSHTTPRequestProperties
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    return chain.proceed(request)

/*    val properties =
      request.tag(LSHTTPRequestProperties::class.java)!!
    val adjProperties =
      properties.copy(target = request.url.toUri())
    val newProperties =
      this.modifier.invoke(adjProperties)
    val newRequest =
      LSOKHTTPRequests.createRequest(newProperties)

    return chain.proceed(newRequest)*/
  }
}
