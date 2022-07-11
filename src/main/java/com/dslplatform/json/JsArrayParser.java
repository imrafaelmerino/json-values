package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

abstract class JsArrayParser extends AbstractParser {

    static final JsArray EMPTY = JsArray.empty();
    private final AbstractParser parser;

    JsArrayParser(final AbstractParser parser) {
        this.parser = parser;
    }

    JsValue nullOrArray(final JsonReader<?> reader,
                        int min,
                        int max) throws IOException {

        return reader.wasNull() ?
               JsNull.NULL :
               array(reader,
                     min,
                     max);
    }

    JsArray array(final JsonReader<?> reader,
                  int min,
                  int max) throws IOException {
        if (checkIfEmpty(isEmptyArray(reader),
                         min,
                         reader.getCurrentIndex())) return EMPTY;
        JsArray buffer = EMPTY.append(parser.value(reader));
        while (reader.getNextToken() == ',') {
            reader.getNextToken();
            buffer = buffer.append(parser.value(reader));
            checkSize(buffer.size() > max,
                      ParserErrors.TOO_LONG_ARRAY.apply(max),
                      reader.getCurrentIndex());

        }
        checkSize(buffer.size() < min,
                  ParserErrors.TOO_SHORT_ARRAY.apply(min),
                  reader.getCurrentIndex());

        reader.checkArrayEnd();
        return buffer;
    }

    private boolean checkIfEmpty(boolean error,
                                 int min,
                                 int reader) {
        if (error) {
            checkSize(min > 0,
                      ParserErrors.EMPTY_ARRAY.apply(min),
                      reader);
            return true;
        }
        return false;
    }

    @Override
    JsArray value(final JsonReader<?> reader) throws IOException {
        if (isEmptyArray(reader)) return EMPTY;
        JsArray buffer = EMPTY.append(parser.value(reader));
        while (reader.getNextToken() == ',') {
            reader.getNextToken();
            buffer = buffer.append(parser.value(reader));
        }
        reader.checkArrayEnd();
        return buffer;

    }

    private boolean isEmptyArray(final JsonReader<?> reader) throws IOException {
        checkSize(reader.last() != '[',
                  ParserErrors.EXPECTING_FOR_LIST_START,
                  reader.getCurrentIndex());
        reader.getNextToken();
        return reader.last() == ']';
    }

    public JsValue nullOrArraySuchThat(final JsonReader<?> reader,
                                       final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) throws IOException {

        return reader.wasNull() ?
               JsNull.NULL :
               arraySuchThat(reader,
                             fn
               );

    }


    JsArray arraySuchThat(final JsonReader<?> reader,
                                 final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) throws IOException {

        final JsArray array = value(reader);
        final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(array);
        if (!result.isPresent()) return array;
        throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                    reader.getCurrentIndex());
    }

    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final IOCallable<JsValue> f,
                              final int min,
                              final int max
    ) throws IOException {

        if (checkIfEmpty(isEmptyArray(reader),
                         min,
                         reader.getCurrentIndex())) return EMPTY;
        JsArray buffer = EMPTY.append(f.call());
        while (reader.getNextToken() == ',') {
            reader.getNextToken();
            buffer = buffer.append(f.call());
            checkSize(buffer.size() > max,
                      ParserErrors.TOO_LONG_ARRAY.apply(max),
                      reader.getCurrentIndex());
        }
        checkSize(buffer.size() < min,
                  ParserErrors.TOO_SHORT_ARRAY.apply(min),
                  reader.getCurrentIndex());

        reader.checkArrayEnd();
        return buffer;

    }

    private void checkSize(boolean error,
                           String message,
                           int reader) {
        if (error)
            throw new JsParserException(message,
                                        reader);
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final IOCallable<JsValue> fn,
                                    final int min,
                                    final int max
    ) throws IOException {

        return reader.wasNull() ?
               JsNull.NULL :
               arrayEachSuchThat(reader,
                                 fn,
                                 min,
                                 max);
    }


}
