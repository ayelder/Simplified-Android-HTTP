package org.librarysimplified.http.api

interface LSHTTPRequestBuilderType {

  enum class AllowRedirects {
    ALLOW_REDIRECTS,
    DISALLOW_REDIRECTS
  }

  fun addHeader(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType

  fun allowRedirects(
    redirects: AllowRedirects
  ): LSHTTPRequestBuilderType

  fun build(): LSHTTPRequestType
}
