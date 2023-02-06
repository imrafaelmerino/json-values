FROM maven:3.8.1-openjdk-17-slim
COPY pom.xml /tmp/json-values/pom.xml
COPY src /tmp/json-values/src/
COPY .mvn /tmp/json-values/.mvn/
COPY settings-template.xml /tmp/json-values/settings-template.xml


WORKDIR /tmp/json-values/
ENV CLASSPATH=/tmp/json-values/target/*