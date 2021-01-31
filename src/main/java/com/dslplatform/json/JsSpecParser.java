package com.dslplatform.json;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsValue;


@FunctionalInterface
public interface JsSpecParser {

    JsValue parse(JsonReader<?> reader);
}
