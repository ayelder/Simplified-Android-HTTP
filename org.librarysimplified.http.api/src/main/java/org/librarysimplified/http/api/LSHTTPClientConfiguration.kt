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

  val applicationVersion: String,

  /**
   * Overrides for TLS-related classes. Typically only useful for unit testing.
   */

  val tlsOverrides: LSHTTPTLSOverrides? = null
)
