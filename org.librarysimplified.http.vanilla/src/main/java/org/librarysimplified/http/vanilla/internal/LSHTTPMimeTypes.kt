package org.librarysimplified.http.vanilla.internal

import one.irradia.mime.api.MIMEType

object LSHTTPMimeTypes {

  val octetStream =
    MIMEType("application", "octet-stream", mapOf())

  val problemReport =
    MIMEType("application", "api-problem+json", mapOf())
}
