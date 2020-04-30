package org.librarysimplified.http.api

import java.net.URI

interface LSHTTPClientType {

  fun newRequest(url: URI): LSHTTPRequestBuilderType

  fun newRequest(url: String): LSHTTPRequestBuilderType =
    this.newRequest(URI.create(url))

}
