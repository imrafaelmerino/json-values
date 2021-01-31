package com.dslplatform.json;

import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.io.IOException;

public final class JsArraySpecParser {
    private final Vector<JsSpecParser> parsers;

    public JsArraySpecParser(final Vector<JsSpecParser> parsers) {
        this.parsers = parsers;
    }

    public JsValue nullOrArray(final JsonReader<?> reader){
        try {
            return reader.wasNull() ? JsNull.NULL : array(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }


    public JsArray array(final JsonReader<?> reader){
        try {
            if (reader.last() != '[') throw reader.newParseError("Expecting '[' for list start");
            reader.getNextToken();
            if (reader.last() == ']') return JsArray.empty();
            JsArray buffer = JsArray.empty();
            int     i      = 0;
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
