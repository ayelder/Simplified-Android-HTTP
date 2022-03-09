package org.librarysimplified.http.oauth_client_credentials

import android.content.Context
import okhttp3.Interceptor
import org.librarysimplified.http.oauth_client_credentials.internal.LSHTTPOAuthClientCredentialsInterceptor
import org.librarysimplified.http.oauth_client_credentials.internal.LSHTTPOAuthClientCredentialsInterceptor.RepositoryKey
import org.librarysimplified.http.oauth_client_credentials.internal.LSHTTPOAuthTokenRepository
import org.librarysimplified.http.vanilla.extensions.LSHTTPInterceptorFactoryType

class LSHTTPOAuthClientCredentialsInterceptors : LSHTTPInterceptorFactoryType {

  private val tokenRepository: LSHTTPOAuthTokenRepository<RepositoryKey, String> =
    LSHTTPOAuthTokenRepository()

  override val name: String =
    "org.librarysimplified.http.oauth_client_credentials"

  override val version: String =
    BuildConfig.OAUTH_CLIENT_CREDENTIALS_VERSION_NAME

  override fun createInterceptor(context: Context): Interceptor {
    return LSHTTPOAuthClientCredentialsInterceptor(tokenRepository)
  }
}
