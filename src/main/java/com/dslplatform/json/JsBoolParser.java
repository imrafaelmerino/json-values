package com.dslplatform.json;

import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

final class JsBoolParser extends AbstractParser {

    @Override
    JsBool value(final JsonReader<?> reader) {
        try {
            if (reader.wasTrue()) return JsBool.TRUE;
            if (reader.wasFalse()) return JsBool.FALSE;
            throw reader.newParseErrorAt(ParserErrors.BOOL_EXPECTED,
                                         reader.getCurrentIndex()
            );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    JsValue nullOrTrue(final JsonReader<?> reader) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   True(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    JsBool True(final JsonReader<?> reader) {
        try {
            if (reader.wasTrue()) return JsBool.TRUE;
            throw reader.newParseErrorAt(ParserErrors.TRUE_EXPECTED,
                                         reader.getCurrentIndex()
            );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    JsValue nullOrFalse(final JsonReader<?> reader) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   False(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    public JsBool False(final JsonReader<?> reader) {
        try {
            if (reader.wasFalse()) return JsBool.FALSE;
            throw reader.newParseErrorAt(ParserErrors.FALSE_EXPECTED,
                                         reader.getCurrentIndex()
            );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }


}
