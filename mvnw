#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.3.2
#
# Optional ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir, required when type is jdk
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
#     e.g. to debug Maven itself, use
#       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_SKIP_RC" ]; then
  if [ -f /usr/local/etc/mavenrc ]; then
    . /usr/local/etc/mavenrc
  fi
  if [ -f /etc/mavenrc ]; then
    . /etc/mavenrc
  fi
  if [ -f "$HOME/.mavenrc" ]; then
    . "$HOME/.mavenrc"
  fi
fi

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
darwin=false
mingw=false
case "$(uname)" in
CYGWIN*) cygwin=true ;;
MINGW*) mingw=true ;;
Darwin*) darwin=true ;;
esac

## Path to the script
# Resolve links: $0 may be a link
SCRIPT="$0"
# Need this for relative symlinks.
while [ -h "$SCRIPT" ]; do
  ls=$(ls -ld "$SCRIPT")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' >/dev/null; then
    SCRIPT="$link"
  else
    SCRIPT="$(dirname "$SCRIPT")/$link"
  fi
done

# Retrieve value of a property from .mvn/wrapper/maven-wrapper.properties
# $1 = file name of the properties file
# $2 = property name to retrieve the value of
get_property_value() {
  grep -E "^$2=" "$1" | tail -1 | cut -d'=' -f2-
}

MAVEN_WRAPPER_PROPERTIES_PATH="$(dirname "$SCRIPT")/.mvn/wrapper/maven-wrapper.properties"

MVN_PATH=$(get_property_value "$MAVEN_WRAPPER_PROPERTIES_PATH" "mvn")
DISTRIBUTION_URL=$(get_property_value "$MAVEN_WRAPPER_PROPERTIES_PATH" "distributionUrl")
WRAPPER_URL=$(get_property_value "$MAVEN_WRAPPER_PROPERTIES_PATH" "wrapperUrl")

if [ -n "$MVN_PATH" ]; then
  exec "$MVN_PATH" "$@"
fi

# Determine Maven home directory
MAVEN_USER_HOME="${MAVEN_USER_HOME:-$HOME/.m2}"
MAVEN_WRAPPER_DIR="$MAVEN_USER_HOME/wrapper"
DISTRIBUTION_ID=$(echo "$DISTRIBUTION_URL" | sed 's|.*/||' | sed 's|\.zip||' | sed 's|\.tar\.gz||')
MAVEN_HOME="$MAVEN_WRAPPER_DIR/$DISTRIBUTION_ID"

# Download Maven if not already present
if [ ! -d "$MAVEN_HOME" ]; then
  mkdir -p "$MAVEN_WRAPPER_DIR"
  echo "Downloading Maven from: $DISTRIBUTION_URL"

  TEMP_DIR=$(mktemp -d)
  TEMP_FILE="$TEMP_DIR/maven.zip"

  if command -v curl >/dev/null 2>&1; then
    curl -fL --progress-bar -o "$TEMP_FILE" "$DISTRIBUTION_URL"
  elif command -v wget >/dev/null 2>&1; then
    wget --progress=bar:force -O "$TEMP_FILE" "$DISTRIBUTION_URL"
  else
    echo "Error: Neither curl nor wget found. Cannot download Maven."
    exit 1
  fi

  unzip -q "$TEMP_FILE" -d "$MAVEN_WRAPPER_DIR" 2>/dev/null || \
    (mkdir -p "$MAVEN_HOME" && tar -xzf "$TEMP_FILE" -C "$MAVEN_WRAPPER_DIR")
  rm -rf "$TEMP_DIR"

  echo "Maven downloaded to: $MAVEN_HOME"
fi

# Find the mvn executable inside the extracted directory
MVN_EXEC=$(find "$MAVEN_HOME" -name "mvn" -type f 2>/dev/null | head -1)

if [ -z "$MVN_EXEC" ]; then
  echo "Error: Could not find mvn executable in $MAVEN_HOME"
  exit 1
fi

exec "$MVN_EXEC" "$@"
