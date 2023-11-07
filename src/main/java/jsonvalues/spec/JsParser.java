package jsonvalues.spec;

import jsonvalues.JsValue;


@FunctionalInterface
interface JsParser {
    JsValue parse(JsReader reader) throws JsParserException;
}
