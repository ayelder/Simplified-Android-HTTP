package org.librarysimplified.http.api

import java.io.Closeable
import java.io.IOException

interface LSHTTPProblemReportParserType : Closeable {

  @Throws(IOException::class)
  fun execute(): LSHTTPProblemReport

}
