package com.dslplatform.json;

import jsonvalues.JsValue;

import java.io.IOException;


@FunctionalInterface
public interface JsSpecParser {
    JsValue parse(JsonReader<?> reader) throws IOException;
}
