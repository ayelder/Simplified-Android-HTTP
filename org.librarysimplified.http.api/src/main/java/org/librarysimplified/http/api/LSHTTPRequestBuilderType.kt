package org.librarysimplified.http.api

import one.irradia.mime.api.MIMEType

/**
 * A mutable request builder.
 */

interface LSHTTPRequestBuilderType {

  /**
   * A specification of whether or not redirects should be allowed.
   */

  enum class AllowRedirects {

    /**
     * Redirects should be followed automatically.
     */

    ALLOW_REDIRECTS,

    /**
     * Redirects should never be followed.
     */

    DISALLOW_REDIRECTS,

    /**
     * Redirects between HTTP and HTTPS are allowed. This is unsafe!
     */

    ALLOW_UNSAFE_REDIRECTS
  }

  /**
   * Add an HTTP header to the request.
   */

  fun addHeader(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType

  /**
   * Remove an HTTP header from the request.
   */

  fun removeHeader(
    name: String
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
   * Set the HTTP authorization. Note that this typically results in the implicit addition of an
   * `Authorization` header to the resulting HTTP request, but an `Authorization` header set
   * explicitly using [addHeader] will take precedence. If this is a problem, use [removeHeader]
   * before calling [setAuthorization] to ensure that no preexisting `Authorization` is used.
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
   * Set a function that is evaluated for each actual HTTP request made to the server. This
   * function will be evaluated at least once, and exactly once for each redirect request.
   */

  fun setModifier(
    modifier: (LSHTTPRequestProperties) -> LSHTTPRequestProperties
  ): LSHTTPRequestBuilderType

  /**
   * Build an immutable request based on the parameters given so far.
   */

  fun build(): LSHTTPRequestType
}
