package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

abstract class JsArrayParser {

    static final JsArray EMPTY = JsArray.empty();
    private final AbstractParser parser;

    public JsArrayParser(final AbstractParser parser) {
        this.parser = parser;
    }


    JsValue nullOrArray(final JsonReader<?> reader) {
        return isNull(reader) ?
               JsNull.NULL :
               array(reader);

    }

    JsValue nullOrArray(final JsonReader<?> reader,
                        int min,
                        int max) {

        return isNull(reader) ?
               JsNull.NULL :
               array(reader,
                     min,
                     max);
    }

    public JsArray array(final JsonReader<?> reader,
                         int min,
                         int max) {
        try {
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
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

    private boolean checkIfEmpty(boolean reader,
                                 int min,
                                 int reader1) {
        if (reader) {
            checkSize(min > 0,
                      ParserErrors.EMPTY_ARRAY.apply(min),
                      reader1);
            return true;
        }
        return false;
    }

    public JsArray array(final JsonReader<?> reader) {
        try {
            if (isEmptyArray(reader)) return EMPTY;
            JsArray buffer = EMPTY.append(parser.value(reader));
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(parser.value(reader));
            }
            reader.checkArrayEnd();
            return buffer;
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

    boolean isEmptyArray(final JsonReader<?> reader) {
        try {
            checkSize(reader.last() != '[',
                      ParserErrors.EXPECTING_FOR_LIST_START,
                      reader.getCurrentIndex());
            reader.getNextToken();
            return reader.last() == ']';
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

    public JsValue nullOrArraySuchThat(final JsonReader<?> reader,
                                       final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) {

        return isNull(reader) ?
               JsNull.NULL :
               arraySuchThat(reader,
                             fn
               );

    }

    private boolean isNull(JsonReader<?> reader) {
        try {
            return reader.wasNull();

        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }

    public JsArray arraySuchThat(final JsonReader<?> reader,
                                 final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) {

        final JsArray array = array(reader);
        final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(array);
        if (!result.isPresent()) return array;
        throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                    reader.getCurrentIndex());
    }

    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final Supplier<JsValue> f,
                              final int min,
                              final int max
    ) {
        try {
            if (checkIfEmpty(isEmptyArray(reader),
                             min,
                             reader.getCurrentIndex())) return EMPTY;

            JsArray buffer = EMPTY.append(f.get());
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(f.get());
                checkSize(buffer.size() > max,
                          ParserErrors.TOO_LONG_ARRAY.apply(max),
                          reader.getCurrentIndex());
            }
            checkSize(buffer.size() < min,
                      ParserErrors.TOO_SHORT_ARRAY.apply(min),
                      reader.getCurrentIndex());

            reader.checkArrayEnd();
            return buffer;
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());

        }
    }

    private void checkSize(boolean buffer,
                           String TOO_LONG_ARRAY,
                           int reader) {
        if (buffer)
            throw new JsParserException(TOO_LONG_ARRAY,
                                        reader);
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final Supplier<JsValue> fn,
                                    final int min,
                                    final int max
    ) {

        return isNull(reader) ?
               JsNull.NULL :
               arrayEachSuchThat(reader,
                                 fn,
                                 min,
                                 max);
    }


}
