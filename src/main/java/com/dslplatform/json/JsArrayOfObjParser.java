package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfObjParser extends JsArrayParser {

    private final JsObjParser parser;

    JsArrayOfObjParser(final JsObjParser parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final Function<JsObj, Optional<JsError>> fn
                                   ){
        try {
            return reader.wasNull() ? JsNull.NULL : arrayEachSuchThat(reader,
                                                                      fn
                                                                     );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }


    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final Function<JsObj, Optional<JsError>> fn
                             ){
        try {
            if (ifIsEmptyArray(reader)) return EMPTY;

            JsArray buffer = EMPTY.append(parser.valueSuchThat(reader,
                                                               fn
                                                              ));
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(parser.valueSuchThat(reader,
                                                            fn
                                                           ));
            }
            reader.checkArrayEnd();
            return buffer;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }


}
