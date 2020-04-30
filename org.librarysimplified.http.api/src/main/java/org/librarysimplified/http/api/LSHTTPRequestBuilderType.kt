package org.librarysimplified.http.api

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
   * Build an immutable request based on the parameters given so far.
   */

  fun build(): LSHTTPRequestType
}
