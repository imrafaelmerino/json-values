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
            throw new JsParserException(ParserErrors.BOOL_EXPECTED,
                                        reader.getCurrentIndex()
            );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }

    JsValue nullOrTrue(final JsonReader<?> reader) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   True(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }

    JsBool True(final JsonReader<?> reader) {
        try {
            if (reader.wasTrue()) return JsBool.TRUE;
            throw new JsParserException(ParserErrors.TRUE_EXPECTED,
                                        reader.getCurrentIndex()
            );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }

    JsValue nullOrFalse(final JsonReader<?> reader) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   False(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }

    public JsBool False(final JsonReader<?> reader) {
        try {
            if (reader.wasFalse()) return JsBool.FALSE;
            throw new JsParserException(ParserErrors.FALSE_EXPECTED,
                                        reader.getCurrentIndex()
            );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }


}
