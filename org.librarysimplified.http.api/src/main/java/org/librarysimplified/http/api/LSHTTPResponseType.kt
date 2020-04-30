package org.librarysimplified.http.api

import java.io.Closeable

interface LSHTTPResponseType : Closeable {

  val status: LSHTTPResponseStatus

}