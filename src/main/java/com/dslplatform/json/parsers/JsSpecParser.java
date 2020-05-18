package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsValue;


@FunctionalInterface
public interface JsSpecParser {

    JsValue parse(JsonReader<?> reader) throws JsParserException;
}
