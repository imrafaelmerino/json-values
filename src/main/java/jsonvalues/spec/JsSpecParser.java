package jsonvalues.spec;

import jsonvalues.JsValue;

import java.io.IOException;


@FunctionalInterface
 interface JsSpecParser {
    JsValue parse(JsonReader reader) throws IOException;
}
