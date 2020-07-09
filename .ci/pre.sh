#!/bin/bash

exec &> >(tee -a ".ci/pre.log")

#------------------------------------------------------------------------
# Utility methods

fatal()
{
  echo "fatal: $1" 1>&2
  echo
  echo "dumping log: " 1>&2
  echo
  cat .ci/pre.log
  exit 1
}

info()
{
  echo "info: $1" 1>&2
}

#------------------------------------------------------------------------
# Check environment

if [ -z "${MAVEN_CENTRAL_USERNAME}" ]
then
  fatal "MAVEN_CENTRAL_USERNAME is not defined"
fi
if [ -z "${MAVEN_CENTRAL_PASSWORD}" ]
then
  fatal "MAVEN_CENTRAL_PASSWORD is not defined"
fi
if [ -z "${MAVEN_CENTRAL_STAGING_PROFILE_ID}" ]
then
  fatal "MAVEN_CENTRAL_STAGING_PROFILE_ID is not defined"
fi

#------------------------------------------------------------------------
# Download Brooklime if necessary.
#

BROOKLIME_URL="https://repo1.maven.org/maven2/com/io7m/brooklime/com.io7m.brooklime.cmdline/0.0.2/com.io7m.brooklime.cmdline-0.0.2-main.jar"
BROOKLIME_SHA256_EXPECTED="abd775e9decd228e543c7ff1f9899183c57cc8b98e1b233e7d46ca03f4ee7e97"

if [ ! -f "brooklime.jar" ]
then
  wget -O "brooklime.jar" "${BROOKLIME_URL}" || fatal "could not download brooklime"
fi

BROOKLIME_SHA256_RECEIVED=$(openssl sha256 "brooklime.jar" | awk '{print $NF}') || fatal "could not checksum brooklime.jar"

if [ "${BROOKLIME_SHA256_EXPECTED}" != "${BROOKLIME_SHA256_RECEIVED}" ]
then
  fatal "brooklime.jar checksum does not match.
  Expected: ${BROOKLIME_SHA256_EXPECTED}
  Received: ${BROOKLIME_SHA256_RECEIVED}"
fi
