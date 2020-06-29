#!/usr/bin/env bash
java -Xms256m \
-jar json-values-benchmark-8.1.0.jar \
-rff jmh-result-deserializers-8.1.0.json \
-rf json \
jsonvalues\.benchmark\.JsDeserializers

