package org.librarysimplified.http.vanilla.internal

import okhttp3.Request
import org.librarysimplified.http.api.LSHTTPRequestBuilderType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.AllowRedirects
import org.librarysimplified.http.api.LSHTTPRequestType

class LSHTTPRequestBuilder(
  private val client: LSHTTPClient,
  private val builder: Request.Builder
) : LSHTTPRequestBuilderType {

  private var redirects: AllowRedirects = AllowRedirects.ALLOW_REDIRECTS

  override fun addHeader(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType {
    this.builder.addHeader(name, value)
    return this
  }

  override fun allowRedirects(
    redirects: AllowRedirects
  ): LSHTTPRequestBuilderType {
    this.redirects = redirects
    return this
  }

  override fun build(): LSHTTPRequestType {
    return LSHTTPRequest(
      client = this.client,
      allowRedirects = this.redirects,
      request = this.builder.build()
    )
  }
}
