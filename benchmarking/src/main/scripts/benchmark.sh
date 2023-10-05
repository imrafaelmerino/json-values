#!/usr/bin/env bash
VERSION=$1
NOW=$(date +"%m-%d-%Y-%T" | tr ':' '_')

java -Xms256m --enable-preview \
-jar releases/json-values-benchmark-${VERSION}.jar \
-rff results/deserializers-${VERSION}-${NOW}.json \
-rf json \
jsonvalues\.benchmark\.JsDeserializers

java -Xms256m --enable-preview \
-jar releases/json-values-benchmark-${VERSION}.jar \
-rff results/serializers-${VERSION}-${NOW}.json \
-rf json \
jsonvalues\.benchmark\.JsSerializers

