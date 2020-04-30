package org.librarysimplified.http.api

import android.content.Context

interface LSHTTPClientProviderType {

  fun create(
    context: Context,
    configuration: LSHTTPClientConfiguration
  ): LSHTTPClientType

}
