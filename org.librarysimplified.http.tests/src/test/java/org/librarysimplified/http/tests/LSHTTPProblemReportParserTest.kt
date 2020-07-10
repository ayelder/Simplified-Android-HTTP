package org.librarysimplified.http.tests

import org.librarysimplified.http.api.LSHTTPProblemReportParserFactoryType
import org.librarysimplified.http.vanilla.LSHTTPProblemReportParsers

class LSHTTPProblemReportParserTest : LSHTTPProblemReportParserContract() {
  override fun parsers(): LSHTTPProblemReportParserFactoryType {
    return LSHTTPProblemReportParsers()
  }
}
