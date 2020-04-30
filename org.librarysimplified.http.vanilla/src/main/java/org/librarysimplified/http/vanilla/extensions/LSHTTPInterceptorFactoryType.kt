package org.librarysimplified.http.vanilla.extensions

import android.content.Context
import okhttp3.Interceptor

interface LSHTTPInterceptorFactoryType {

  val name: String

  val version: String

  fun createInterceptor(
    context: Context
  ): Interceptor
}
