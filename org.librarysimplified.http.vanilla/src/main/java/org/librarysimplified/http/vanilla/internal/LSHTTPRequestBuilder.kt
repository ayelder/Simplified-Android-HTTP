package org.librarysimplified.http.vanilla.internal

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import one.irradia.mime.api.MIMEType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.AllowRedirects
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.DELETE
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.GET
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.HEAD
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.POST
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.PUT
import org.librarysimplified.http.api.LSHTTPRequestType

class LSHTTPRequestBuilder(
  private val client: LSHTTPClient,
  private val builder: Request.Builder
) : LSHTTPRequestBuilderType {

  private var bodyContent: MIMEType? = null
  private var body: ByteArray? = null
  private var method: Method = GET
  private var redirects: AllowRedirects = AllowRedirects.ALLOW_REDIRECTS

  init {
    this.setMethod(this.method)
  }

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

  override fun setMethod(
    method: Method
  ): LSHTTPRequestBuilderType {
    this.method = method
    return this
  }

  override fun setBody(
    body: ByteArray,
    contentType: MIMEType
  ): LSHTTPRequestBuilderType {
    this.body = body
    this.bodyContent = contentType
    return this
  }

  override fun build(): LSHTTPRequestType {
    when (this.method) {
      GET -> {
        this.body = null
        this.bodyContent = null
        this.builder.get()
      }
      HEAD -> {
        this.body = null
        this.bodyContent = null
        this.builder.head()
      }
      POST -> {
        val bytes = this.body ?: ByteArray(0)
        val type = this.bodyContent?.fullType ?: LSHTTPMimeTypes.octetStream.fullType
        this.builder.post(bytes.toRequestBody(type.toMediaType()))
      }
      PUT -> {
        val bytes = this.body ?: ByteArray(0)
        val type = this.bodyContent?.fullType ?: LSHTTPMimeTypes.octetStream.fullType
        this.builder.put(bytes.toRequestBody(type.toMediaType()))
      }
      DELETE -> {
        this.body = null
        this.bodyContent = null
        this.builder.delete()
      }
    }

    return LSHTTPRequest(
      client = this.client,
      allowRedirects = this.redirects,
      request = this.builder.build()
    )
  }
}
