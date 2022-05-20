package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsArrayOfIntParser extends JsArrayParser {
    private final JsIntParser parser;

    JsArrayOfIntParser(final JsIntParser parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final IntFunction<Optional<JsError>> fn,
                                    final int min,
                                    final int max
    ) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   arrayEachSuchThat(reader,
                                     fn,
                                     min,
                                     max
                   );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }

    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final IntFunction<Optional<JsError>> fn,
                              final int min,
                              final int max
    ) {
        try {
            if (ifIsEmptyArray(reader)) {
                if (min > 0) throw reader.newParseError(ParserErrors.A.apply(min),
                                                        reader.getCurrentIndex());
                return EMPTY;
            }

            JsArray buffer = EMPTY.append(parser.valueSuchThat(reader,
                                                               fn));
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(parser.valueSuchThat(reader,
                                                            fn));
                if (buffer.size() > max)
                    throw reader.newParseError(ParserErrors.B.apply(min),
                                               reader.getCurrentIndex()
                    );
            }
            if (buffer.size() < min)
                throw reader.newParseError(ParserErrors.C.apply(min),
                                           reader.getCurrentIndex());
            reader.checkArrayEnd();
            return buffer;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }

}
