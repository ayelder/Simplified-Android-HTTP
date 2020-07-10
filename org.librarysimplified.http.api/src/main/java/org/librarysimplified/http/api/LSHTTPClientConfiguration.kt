package org.librarysimplified.http.api

/**
 * The basic client configuration.
 */

data class LSHTTPClientConfiguration(

  /**
   * The application name (for User-Agent strings).
   */

  val applicationName: String,

  /**
   * The application version (for User-Agent strings).
   */

  val applicationVersion: String
)
