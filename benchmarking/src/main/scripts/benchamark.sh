#!/usr/bin/env bash
VERSION=11.6.0
NOW=$(date +"%m-%d-%Y-%T" | tr ':' '_')

java -Xms256m \
-jar releases/json-values-benchmark-${VERSION}.jar \
-rff results/deserializers-${VERSION}-${NOW}.json \
-rf json \
jsonvalues\.benchmark\.JsDeserializers

java -Xms256m \
-jar releases/json-values-benchmark-${VERSION}.jar \
-rff results/serializers-${VERSION}-${NOW}.json \
-rf json \
jsonvalues\.benchmark\.JsSerializers

