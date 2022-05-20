package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.List;

public final class JsArraySpecParser {
    private final List<JsSpecParser> parsers;

    public JsArraySpecParser(final List<JsSpecParser> parsers) {
        this.parsers = parsers;
    }

    public JsValue nullOrArray(final JsonReader<?> reader) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   array(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }


    public JsArray array(final JsonReader<?> reader) {
        try {
            if (reader.last() != '[') throw reader.newParseError(ParserErrors.EXPECTING_FOR_LIST_START,
                                                                 reader.getCurrentIndex());
            reader.getNextToken();
            if (reader.last() == ']') return JsArray.empty();
            JsArray buffer = JsArray.empty();
            int i = 0;
            buffer = buffer.append(parsers.get(i)
                                          .parse(reader));
            while (reader.getNextToken() == ',') {
                i = i + 1;
                reader.getNextToken();
                buffer = buffer.append(parsers.get(i)
                                              .parse(reader));

            }
            reader.checkArrayEnd();
            return buffer;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }


}
