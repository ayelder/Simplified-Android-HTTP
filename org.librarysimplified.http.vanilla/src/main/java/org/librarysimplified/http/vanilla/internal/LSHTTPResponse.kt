package org.librarysimplified.http.vanilla.internal

import okhttp3.Response
import okhttp3.internal.closeQuietly
import one.irradia.mime.api.MIMEType
import org.librarysimplified.http.api.LSHTTPResponseStatus
import org.librarysimplified.http.api.LSHTTPResponseType
import java.io.InputStream

class LSHTTPResponse(
  val request: LSHTTPRequest,
  override val status: LSHTTPResponseStatus,
  val response: Response?
) : LSHTTPResponseType {

  override fun close() {
    this.response?.closeQuietly()
  }
}
