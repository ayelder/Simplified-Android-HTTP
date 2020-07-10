package org.librarysimplified.http.api

/**
 * An RFC 7807 problem report.
 *
 * @see "https://tools.ietf.org/html/rfc7807"
 */

data class LSHTTPProblemReport(
  val status: Int?,
  val title: String?,
  val detail: String?,
  val type: String?
)
