package jsonvalues.spec;

import jsonvalues.JsBool;


final class JsBoolReader extends AbstractReader {

  @Override
  JsBool value(final DslJsReader reader) throws JsParserException {

    if (reader.wasTrue()) {
      return JsBool.TRUE;
    }
    if (reader.wasFalse()) {
      return JsBool.FALSE;
    }
    throw JsParserException.reasonAt(ParserErrors.BOOL_EXPECTED,
                                     reader.getPositionInStream()
                                    );
  }

}
