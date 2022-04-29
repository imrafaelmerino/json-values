package com.dslplatform.json;

import jsonvalues.JsValue;


@FunctionalInterface
public interface JsSpecParser {

    JsValue parse(JsonReader<?> reader);
}
