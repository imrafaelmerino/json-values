#!/usr/bin/env bash
#BY THIS TIME (1-JULY-2019) JAVA 8 IS REQUIRED
JAVA_PATH=${HOME}/.sdkman/candidates/java/8.0.181-oracle/bin/java
CURRENT_DIR=$(dirname "$0")
LIB_DIR=${CURRENT_DIR}/libs
WORK_DIR=${CURRENT_DIR}/.tmp
PROJECT_HOME=${CURRENT_DIR}/../src/main/java
CLASSPATH=${LIB_DIR}/scala-library-2.12.8.jar

mkdir -p ${WORK_DIR}/classes/
rm -rf ${WORK_DIR}/classes/*
rm ${WORK_DIR}/sources.txt
find ${PROJECT_HOME} -name "*.java" > ${WORK_DIR}/sources.txt

${JAVA_PATH} -Dfile.encoding=UTF8 -jar ${LIB_DIR}/checker-framework-2.8.2/checker/dist/checker.jar \
-processor org.checkerframework.checker.nullness.NullnessChecker \
-cp "${CLASSPATH}" \
-d ${WORK_DIR}/classes/ \
@/${WORK_DIR}/sources.txt