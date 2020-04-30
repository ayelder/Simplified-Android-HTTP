package org.librarysimplified.http.api

import java.net.URI

/**
 * An HTTP client.
 *
 * Typically, applications should create a single client and reuse that client perpetually.
 * Clients are expected to be thread-safe.
 */

interface LSHTTPClientType {

  /**
   * Create a new request builder for the given URI.
   */

  fun newRequest(url: URI): LSHTTPRequestBuilderType

  /**
   * Create a new request builder for the given URI.
   */

  fun newRequest(url: String): LSHTTPRequestBuilderType =
    this.newRequest(URI.create(url))

}
