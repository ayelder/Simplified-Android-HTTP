package org.librarysimplified.http.api

import one.irradia.mime.api.MIMEType
import java.io.InputStream

/**
 * The type of response status values. A response can be one of three types:
 *
 * 1. The request couldn't be sent due to a network or other I/O error.
 * 2. The request was sent and the server responded with an error code.
 * 3. The request was sent and the server responded with a successful status code.
 */

sealed class LSHTTPResponseStatus {

  /**
   * The type of responses that indicate the server returned something.
   */

  sealed class Responded : LSHTTPResponseStatus() {

    /** The HTTP status code, possibly modified by any [LSHTTPProblemReport] returned. */
    abstract val status: Int
    /** The original HTTP status code. */
    abstract val originalStatus: Int
    /** The server message line (such as "UNAUTHORIZED"). */
    abstract val message: String
    /** The parsed server content type. */
    abstract val contentType: MIMEType
    /** The length of the content, if available. */
    abstract val contentLength: Long?
    /** The RFC7807 problem report, if one was provided. */
    abstract val problemReport: LSHTTPProblemReport?
    /** The stream of data returned as the HTTP response body */
    abstract val bodyStream: InputStream?

    /**
     * The server responded with a successful status code.
     */

    data class OK(
      override val status: Int,
      override val originalStatus: Int,
      override val message: String,
      override val contentType: MIMEType,
      override val contentLength: Long?,
      override val problemReport: LSHTTPProblemReport?,
      override val bodyStream: InputStream?
    ) : Responded()

    /**
     * The server responded with an error code.
     */

    data class Error(
      override val status: Int,
      override val originalStatus: Int,
      override val message: String,
      override val contentType: MIMEType,
      override val contentLength: Long?,
      override val problemReport: LSHTTPProblemReport?,
      override val bodyStream: InputStream?
    ) : Responded()
  }

  /**
   * The request failed to yield a response due to the given exception.
   */

  data class Failed(
    val exception: Exception
  ) : LSHTTPResponseStatus()
}
