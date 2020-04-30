#!/bin/sh

#------------------------------------------------------------------------
# Execute the build

./gradlew clean test assemble \
  -Dorg.gradle.internal.publish.checksums.insecure=true

