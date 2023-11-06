package jsonvalues.spec;

import jsonvalues.JsParserException;
import jsonvalues.JsValue;



@FunctionalInterface
interface JsParser {
    JsValue parse(JsReader reader) throws JsParserException;
}
