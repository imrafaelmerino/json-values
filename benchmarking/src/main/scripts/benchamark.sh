#!/usr/bin/env bash
java -Xms256m \
-jar releases/8.1.0/benchmark-8.1.0-open-8.jar \
-rff results/8.1.0/open/deserializer/open-r9-c8.json \
-rf json \
jsonvalues\.benchmark\.JsDeserializers

java -Xms256m \
-jar releases/8.1.0/benchmark-8.1.0-open-8.jar \
-rff results/8.1.0/open/serializer/open-r9-c8.json \
-rf json \
jsonvalues\.benchmark\.JsSerializers

java -Xms256m \
-jar releases/8.1.0/benchmark-8.1.0-open-9.jar \
-rff results/8.1.0/open/deserializer/open-r9-c9.json \
-rf json \
jsonvalues\.benchmark\.JsDeserializers

java -Xms256m \
-jar releases/8.1.0/benchmark-8.1.0-open-9.jar \
-rff results/8.1.0/open/serializer/open-r9-c9.json \
-rf json \
jsonvalues\.benchmark\.JsSerializers

#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-10.jar \
#-rff results/8.1.0/open/deserializer/open-r11-c10.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-10.jar \
#-rff results/8.1.0/open/serializer/open-r11-c10.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-11.jar \
#-rff results/8.1.0/open/deserializer/open-r11-c11.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-11.jar \
#-rff results/8.1.0/open/serializer/open-r11-c11.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers

#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-12.jar \
#-rff results/8.1.0/open/deserializer/open-r11-c12.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-12.jar \
#-rff results/8.1.0/open/serializer/open-r11-c12.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-13.jar \
#-rff results/8.1.0/open/deserializer/open-r11-c13.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-13.jar \
#-rff results/8.1.0/open/serializer/open-r11-c13.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-14.jar \
#-rff results/8.1.0/open/deserializer/open-r11-c14.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/8.1.0/benchmark-8.1.0-open-14.jar \
#-rff results/8.1.0/open/serializer/open-r11-c14.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers

