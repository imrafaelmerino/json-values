package jsonvalues.spec;

import jsonvalues.JsValue;


@FunctionalInterface
interface JsParser {

  JsValue parse(DslJsReader reader) throws JsParserException;
}
