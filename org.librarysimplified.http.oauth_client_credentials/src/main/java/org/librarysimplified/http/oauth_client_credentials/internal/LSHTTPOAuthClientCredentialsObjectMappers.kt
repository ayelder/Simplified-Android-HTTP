package org.librarysimplified.http.oauth_client_credentials.internal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule

object LSHTTPOAuthClientCredentialsObjectMappers {

  fun createObjectMapper(): ObjectMapper {
    val mapper = JsonMapper.builder()
      .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .build()
    val deserializers = LSHTTPOAuthClientCredentialsDeserializers.create()
    val simpleModule = SimpleModule()
    simpleModule.setDeserializers(deserializers)
    mapper.registerModule(simpleModule)
    return mapper
  }
}
