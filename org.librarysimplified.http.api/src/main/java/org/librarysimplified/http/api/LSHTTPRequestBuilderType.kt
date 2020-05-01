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

  enum class Method {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE
  }

  /**
   * Set the HTTP method used. The default is [Method.GET].
   */

  fun setMethod(
    method: Method
  ): LSHTTPRequestBuilderType

  /**
   * Set the request body.
   */

  fun setBody(
    body: ByteArray,
    contentType: MIMEType
  ): LSHTTPRequestBuilderType

  /**
   * Build an immutable request based on the parameters given so far.
   */

  fun build(): LSHTTPRequestType
}
