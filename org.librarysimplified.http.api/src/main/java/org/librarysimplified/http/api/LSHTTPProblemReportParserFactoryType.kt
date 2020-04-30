package org.librarysimplified.http.api

import java.io.InputStream

interface LSHTTPProblemReportParserFactoryType {

  fun createParser(
    uri: String,
    stream: InputStream
  ): LSHTTPProblemReportParserType

}