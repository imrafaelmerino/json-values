package com.dslplatform.json;

import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;


final class JsBoolParser extends AbstractParser {

    @Override
    JsBool value(final JsonReader<?> reader) throws ParsingException {

        if (reader.wasTrue()) return JsBool.TRUE;
        if (reader.wasFalse()) return JsBool.FALSE;
        throw new JsParserException(ParserErrors.BOOL_EXPECTED,
                                    reader.getCurrentIndex()
        );

    }

    JsValue nullOrTrue(final JsonReader<?> reader) throws ParsingException {
        return reader.wasNull() ?
               JsNull.NULL :
               True(reader);
    }

    JsBool True(final JsonReader<?> reader) throws ParsingException {
        if (reader.wasTrue()) return JsBool.TRUE;
        throw new JsParserException(ParserErrors.TRUE_EXPECTED,
                                    reader.getCurrentIndex()
        );

    }

    JsValue nullOrFalse(final JsonReader<?> reader) throws ParsingException {
        return reader.wasNull() ?
               JsNull.NULL :
               False(reader);

    }

    public JsBool False(final JsonReader<?> reader) throws ParsingException {

        if (reader.wasFalse()) return JsBool.FALSE;
        throw new JsParserException(ParserErrors.FALSE_EXPECTED,
                                    reader.getCurrentIndex()
        );

    }


}
