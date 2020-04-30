package org.librarysimplified.http.api

/**
 * An immutable request.
 */

interface LSHTTPRequestType {

  /**
   * Execute the request on the client that created it.
   */

  fun execute(): LSHTTPResponseType

}
