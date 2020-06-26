#!/usr/bin/env bash
java -Xms256m \
-jar json-values-benchmark-8.0.2.jar \
-rff jmh-result-serializers-8.0.2.json \
-rf json \
jsonvalues\.benchmark\.JsSerializers

