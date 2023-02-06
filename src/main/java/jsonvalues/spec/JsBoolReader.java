package jsonvalues.spec;

import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;


final class JsBoolReader extends AbstractReader {

    @Override
    JsBool value(final JsonReader reader) throws JsParserException {

        if (reader.wasTrue()) return JsBool.TRUE;
        if (reader.wasFalse()) return JsBool.FALSE;
        throw JsParserException.create(ParserErrors.BOOL_EXPECTED,
                                       reader.getCurrentIndex(),
                                       false
                                      );
    }

    JsValue nullOrTrue(final JsonReader reader) throws JsParserException {
        return reader.wasNull() ?
                JsNull.NULL :
                True(reader);
    }

    JsBool True(final JsonReader reader) throws JsParserException {
        if (reader.wasTrue()) return JsBool.TRUE;
        throw JsParserException.create(ParserErrors.TRUE_EXPECTED,
                                       reader.getCurrentIndex(),
                                       false
                                      );

    }

    JsValue nullOrFalse(final JsonReader reader) throws JsParserException {
        return reader.wasNull() ?
                JsNull.NULL :
                False(reader);

    }

    JsBool False(final JsonReader reader) throws JsParserException {

        if (reader.wasFalse()) return JsBool.FALSE;
        throw JsParserException.create(ParserErrors.FALSE_EXPECTED,
                                       reader.getCurrentIndex(),
                                       false
                                      );

    }


}
