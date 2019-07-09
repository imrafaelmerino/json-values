#!/usr/bin/env bash
CURRENT_DIR=$(dirname "$0")
JAVA_BIN=${HOME}/.sdkman/candidates/java/9.0.1-oracle/bin
JAR_PATH="${1:-$CURRENT_DIR/../target/json-values-1.0.0-SNAPSHOT.jar}"
${JAVA_BIN}/jshell -v --startup ${CURRENT_DIR}/startup.jsh --class-path ${JAR_PATH}
