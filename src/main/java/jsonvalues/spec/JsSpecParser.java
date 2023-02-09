package jsonvalues.spec;

import jsonvalues.JsValue;

import java.io.IOException;


@FunctionalInterface
 interface JsSpecParser {
    JsValue parse(JsReader reader) throws IOException;
}
