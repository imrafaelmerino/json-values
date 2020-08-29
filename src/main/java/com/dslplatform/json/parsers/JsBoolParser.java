package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

final class JsBoolParser extends AbstractParser {

    JsBool value(final JsonReader<?> reader){
        try {
            if (reader.wasTrue()) return JsBool.TRUE;
            else if (reader.wasFalse()) return JsBool.FALSE;
            throw reader.newParseErrorAt("Found invalid boolean value",
                                         0
                                        );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    JsValue nullOrTrue(final JsonReader<?> reader){
        try {
            return reader.wasNull() ? JsNull.NULL : True(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    JsBool True(final JsonReader<?> reader){
        try {
            if (reader.wasTrue()) return JsBool.TRUE;
            throw reader.newParseErrorAt("Found invalid boolean value. True was expected.",
                                         0
                                        );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    JsValue nullOrFalse(final JsonReader<?> reader){
        try {
            return reader.wasNull() ? JsNull.NULL : False(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    public JsBool False(final JsonReader<?> reader){
        try {
            if (reader.wasFalse()) return JsBool.FALSE;
            throw reader.newParseErrorAt("Found invalid boolean value. False was expected.",
                                         0
                                        );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }


}
