package org.librarysimplified.http.vanilla.internal

import org.librarysimplified.http.api.LSHTTPAuthorizationType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.AllowRedirects
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method
import org.librarysimplified.http.api.LSHTTPRequestBuilderType.Method.Get
import org.librarysimplified.http.api.LSHTTPRequestProperties
import org.librarysimplified.http.api.LSHTTPRequestType
import java.net.MalformedURLException
import java.net.URI
import java.util.TreeMap

class LSHTTPRequestBuilder(
  private val client: LSHTTPClient,
  private val target: URI
) : LSHTTPRequestBuilderType {

  private var modifier: (LSHTTPRequestProperties) -> LSHTTPRequestProperties = { it }
  private var redirects: AllowRedirects = AllowRedirects.ALLOW_REDIRECTS

  private var properties =
    LSHTTPRequestProperties(
      target = this.target,
      cookies = sortedMapOf(),
      headers = sortedMapOf(),
      method = Get,
      authorization = null
    )

  init {
    try {
      this.target.toURL()
    } catch (e: MalformedURLException) {
      throw IllegalArgumentException(e)
    }
  }

  override fun addHeader(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType {
    val oldHeaders = TreeMap(this.properties.headers)
    oldHeaders[name] = value
    this.properties = this.properties.copy(headers = oldHeaders.toSortedMap())
    return this
  }

  override fun removeHeader(
    name: String
  ): LSHTTPRequestBuilderType {
    val oldHeaders = TreeMap(this.properties.headers)
    oldHeaders.remove(name)
    this.properties = this.properties.copy(headers = oldHeaders.toSortedMap())
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
    this.properties = this.properties.copy(method = method)
    return this
  }

  override fun setAuthorization(
    authorization: LSHTTPAuthorizationType?
  ): LSHTTPRequestBuilderType {
    this.properties = this.properties.copy(authorization = authorization)
    return this
  }

  override fun addCookie(
    name: String,
    value: String
  ): LSHTTPRequestBuilderType {
    val oldCookies = TreeMap(this.properties.cookies)
    oldCookies[name] = value
    this.properties = this.properties.copy(cookies = oldCookies.toSortedMap())
    return this
  }

  override fun removeCookie(
    name: String
  ): LSHTTPRequestBuilderType {
    val oldCookies = TreeMap(this.properties.cookies)
    oldCookies.remove(name)
    this.properties = this.properties.copy(cookies = oldCookies.toSortedMap())
    return this
  }

  override fun removeAllCookies(): LSHTTPRequestBuilderType {
    this.properties = this.properties.copy(cookies = sortedMapOf())
    return this
  }

  override fun setModifier(
    modifier: (LSHTTPRequestProperties) -> LSHTTPRequestProperties
  ): LSHTTPRequestBuilderType {
    this.modifier = modifier
    return this
  }

  override fun build(): LSHTTPRequestType {
    return LSHTTPRequest(
      client = this.client,
      allowRedirects = this.redirects,
      modifier = this.modifier,
      properties = this.properties
    )
  }
}
