package org.librarysimplified.http.api

import one.irradia.mime.api.MIMEType
import java.io.InputStream

sealed class LSHTTPResponseStatus {

  sealed class Responded : LSHTTPResponseStatus() {

    abstract val status: Int
    abstract val originalStatus: Int
    abstract val message: String
    abstract val contentType: MIMEType
    abstract val contentLength: Long?
    abstract val problemReport: LSHTTPProblemReport?
    abstract val bodyStream: InputStream?

    data class OK(
      override val status: Int,
      override val originalStatus: Int,
      override val message: String,
      override val contentType: MIMEType,
      override val contentLength: Long?,
      override val problemReport: LSHTTPProblemReport?,
      override val bodyStream: InputStream?
    ) : Responded()

    data class Error(
      override val status: Int,
      override val originalStatus: Int,
      override val message: String,
      override val contentType: MIMEType,
      override val contentLength: Long?,
      override val problemReport: LSHTTPProblemReport?,
      override val bodyStream: InputStream?
    ) : Responded()
  }

  data class Failed(
    val exception: Exception
  ) : LSHTTPResponseStatus()

}
