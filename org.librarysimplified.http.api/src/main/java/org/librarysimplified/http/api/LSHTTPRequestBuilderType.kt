package org.librarysimplified.http.api

import one.irradia.mime.api.MIMEType

/**
 * A mutable request builder.
 */

interface LSHTTPRequestBuilderType {

  /**
   * A specification of whether or not redirects should be allowed. The API never allows
   * redirecting from an HTTPS address to an HTTP address, for security reasons.
   */

  enum class AllowRedirects {

    /**
     * Redirects should be followed automatically.
     */

    ALLOW_REDIRECTS,

    /**
     * Redirects should never be followed.
     */

    DISALLOW_REDIRECTS
  }

  /**
   * Add an HTTP header to the request.
   */

  fun addHeader(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType

  /**
   * Specify redirect behaviour. The default is [AllowRedirects.ALLOW_REDIRECTS].
   */

  fun allowRedirects(
    redirects: AllowRedirects
  ): LSHTTPRequestBuilderType

  /**
   * The HTTP method used.
   */

  sealed class Method {
    object Get : Method()
    object Head : Method()
    object Delete : Method()

    data class Post(
      val body: ByteArray,
      val contentType: MIMEType
    ) : Method()

    data class Put(
      val body: ByteArray,
      val contentType: MIMEType
    ) : Method()
  }

  /**
   * Set the HTTP method used. The default is [Method.Get].
   */

  fun setMethod(
    method: Method
  ): LSHTTPRequestBuilderType

  /**
   * Set the HTTP authorization.
   */

  fun setAuthorization(
    authorization: LSHTTPAuthorizationType?
  ): LSHTTPRequestBuilderType

  /**
   * Add a cookie to the request.
   */

  fun addCookie(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType

  /**
   * Remove a cookie from the request.
   */

  fun removeCookie(
    name: String
  ): LSHTTPRequestBuilderType

  /**
   * Remove all cookies from the request.
   */

  fun removeAllCookies(): LSHTTPRequestBuilderType

  /**
   * Build an immutable request based on the parameters given so far.
   */

  fun build(): LSHTTPRequestType
}
