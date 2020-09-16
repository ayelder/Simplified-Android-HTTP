package org.librarysimplified.http.bearer_token

import android.content.Context
import okhttp3.Interceptor
import org.librarysimplified.http.bearer_token.internal.LSHTTPBearerTokenInterceptor
import org.librarysimplified.http.vanilla.extensions.LSHTTPInterceptorFactoryType

/**
 * An interceptor that can transparently follow and use Simplified bearer tokens.
 */

class LSHTTPBearerTokenInterceptors : LSHTTPInterceptorFactoryType {

  companion object {

    /**
     * The content type of Simplified bearer tokens.
     */

    const val bearerTokenContentType =
      "application/vnd.librarysimplified.bearer-token+json"
  }

  override val name: String =
    "org.librarysimplified.http.bearer_token"

  override val version: String =
    BuildConfig.BEARER_TOKEN_VERSION_NAME

  override fun createInterceptor(context: Context): Interceptor {
    return LSHTTPBearerTokenInterceptor()
  }
}
