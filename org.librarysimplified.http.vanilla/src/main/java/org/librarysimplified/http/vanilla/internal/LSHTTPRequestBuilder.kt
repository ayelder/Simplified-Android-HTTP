package org.librarysimplified.http.vanilla.internal

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.librarysimplified.http.api.LSHTTPAuthorizationType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.AllowRedirects
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.Delete
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.Get
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.Head
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.Post
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.Put
import org.librarysimplified.http.api.LSHTTPRequestType
import java.util.TreeMap

class LSHTTPRequestBuilder(
  private val client: LSHTTPClient,
  private val builder: Request.Builder
) : LSHTTPRequestBuilderType {

  private val cookies = TreeMap<String, String>()
  private var authorization: LSHTTPAuthorizationType? = null
  private var method: Method = Get
  private var redirects: AllowRedirects = AllowRedirects.ALLOW_REDIRECTS

  init {
    this.setMethod(this.method)
    this.setAuthorization(null)
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

  override fun setAuthorization(
    authorization: LSHTTPAuthorizationType?
  ): LSHTTPRequestBuilderType {
    this.authorization = authorization
    if (authorization != null) {
      this.builder.header("Authorization", authorization.toHeaderValue())
    } else {
      this.builder.removeHeader("Authorization")
    }
    return this
  }

  override fun addCookie(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType {
    this.cookies[name] = value
    return this
  }

  override fun removeCookie(
    name: String
  ): LSHTTPRequestBuilderType {
    this.cookies.remove(name)
    return this
  }

  override fun removeAllCookies(): LSHTTPRequestBuilderType {
    this.cookies.clear()
    return this
  }

  override fun build(): LSHTTPRequestType {
    if (this.cookies.isNotEmpty()) {
      val headerText =
        this.cookies.entries.fold(
          initial = "",
          operation = { acc, entry -> acc + "${entry.key}=${entry.value};" }
        )
      this.builder.header("Cookie", headerText)
    }

    when (val method = this.method) {
      Get -> {
        this.builder.get()
      }
      Head -> {
        this.builder.head()
      }
      is Post -> {
        val bytes = method.body
        val type = method.contentType.fullType
        this.builder.post(bytes.toRequestBody(type.toMediaType()))
      }
      is Put -> {
        val bytes = method.body
        val type = method.contentType.fullType
        this.builder.put(bytes.toRequestBody(type.toMediaType()))
      }
      Delete -> {
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
