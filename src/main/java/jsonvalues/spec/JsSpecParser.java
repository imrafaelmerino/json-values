package jsonvalues.spec;

import jsonvalues.JsParserException;
import jsonvalues.JsValue;



@FunctionalInterface
interface JsSpecParser {
    JsValue parse(JsReader reader) throws JsParserException;
}
