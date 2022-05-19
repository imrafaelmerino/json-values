package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

abstract class JsArrayParser {

    static final JsArray EMPTY = JsArray.empty();
    private final AbstractParser parser;

    public JsArrayParser(final AbstractParser parser) {
        this.parser = parser;
    }

    JsValue nullOrArray(final JsonReader<?> reader) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   array(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsValue nullOrArray(final JsonReader<?> reader,
                        int min,
                        int max) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   array(reader,
                         min,
                         max);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    public JsArray array(final JsonReader<?> reader,
                         int min,
                         int max) {
        try {
            if (ifIsEmptyArray(reader)) {
                if (min > 0) throw reader.newParseError(ParserConf.A.apply(min),
                                                        reader.getCurrentIndex());
                return EMPTY;
            }
            JsArray buffer = EMPTY.append(parser.value(reader));
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(parser.value(reader));
                if (buffer.size() > max)
                    throw reader.newParseError(ParserConf.B.apply(min),
                                               reader.getCurrentIndex()
                                               );
            }
            if (buffer.size() < min)
                throw reader.newParseError(ParserConf.C.apply(min),
                                           reader.getCurrentIndex());

            reader.checkArrayEnd();
            return buffer;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    public JsArray array(final JsonReader<?> reader) {
        try {
            if (ifIsEmptyArray(reader)) return EMPTY;
            JsArray buffer = EMPTY.append(parser.value(reader));
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(parser.value(reader));
            }
            reader.checkArrayEnd();
            return buffer;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    boolean ifIsEmptyArray(final JsonReader<?> reader) {
        try {
            if (reader.last() != '[')
                throw reader.newParseError(ParserConf.EXPECTING_FOR_LIST_START,
                                           reader.getCurrentIndex());
            reader.getNextToken();
            return reader.last() == ']';
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }


    public JsValue nullOrArraySuchThat(final JsonReader<?> reader,
                                       final Function<JsArray, Optional<JsError>> fn
    ) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   arraySuchThat(reader,
                                 fn
                   );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    public JsArray arraySuchThat(final JsonReader<?> reader,
                                 final Function<JsArray, Optional<JsError>> fn
    ) {
        try {
            final JsArray array = array(reader);
            final Optional<JsError> result = fn.apply(array);
            if (!result.isPresent()) return array;
            throw reader.newParseError(ParserConf.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }

    }

}
