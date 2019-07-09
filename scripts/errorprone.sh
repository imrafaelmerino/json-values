#!/usr/bin/env bash

#THIS SCRIPT HAS BEEN TESTED WITH JAVA 9 FROM ORACLE
JAVA_PATH=${HOME}/.sdkman/candidates/java/9.0.1-oracle/bin/javac

CURRENT_DIR=$(dirname "$0")
LIB_DIR=${CURRENT_DIR}/libs
WORK_DIR=${CURRENT_DIR}/.tmp
PROJECT_HOME=${CURRENT_DIR}/../src/main/java
CLASSPATH="${LIB_DIR}/scala-library-2.12.8.jar:${LIB_DIR}/checker-framework-2.8.2/checker/dist/*"

mkdir -p ${WORK_DIR}/classes/
rm -rf ${WORK_DIR}/classes/*
rm ${WORK_DIR}/sources.txt
find ${PROJECT_HOME} -name "*.java" > ${WORK_DIR}/sources.txt

${JAVA_PATH} \
-encoding utf8 \
-XDcompilePolicy=simple \
-processorpath ${LIB_DIR}/failureaccess-1.0.jar:${LIB_DIR}/error_prone_core-2.3.3-with-dependencies.jar:${LIB_DIR}/dataflow-2.5.7.jar:${LIB_DIR}/javacutil-2.5.7.jar:jFormatString-3.0.0.jar \
'-Xplugin:ErrorProne -XepDisableAllChecks -Xep:CollectionIncompatibleType:ERROR' \
-cp "${CLASSPATH}" \
-d ${WORK_DIR}/classes/ \
@/${WORK_DIR}/sources.txt

