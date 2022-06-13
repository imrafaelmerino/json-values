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
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   array(reader);
        } catch (ParsingException e) {
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
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
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
        }


    }

    private JsParserException dslExc2MyExp(String message,
                                           int index) {
        return new JsParserException(message,
                                     index);
    }

    public JsArray array(final JsonReader<?> reader,
                         int min,
                         int max) {
        try {
            if (ifIsEmptyArray(reader)) {
                if (min > 0)
                    throw dslExc2MyExp(ParserErrors.EMPTY_ARRAY.apply(min),
                                       reader.getCurrentIndex());
                return EMPTY;
            }
            JsArray buffer = EMPTY.append(parser.value(reader));
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(parser.value(reader));
                if (buffer.size() > max)
                    throw dslExc2MyExp(ParserErrors.TOO_LONG_ARRAY.apply(max),
                                       reader.getCurrentIndex());

            }
            if (buffer.size() < min)
                throw dslExc2MyExp(ParserErrors.TOO_SHORT_ARRAY.apply(min),
                                   reader.getCurrentIndex());

            reader.checkArrayEnd();
            return buffer;
        } catch (ParsingException e) {
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
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
        } catch (ParsingException e) {
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

    boolean ifIsEmptyArray(final JsonReader<?> reader) {
        try {
            if (reader.last() != '[')
                throw dslExc2MyExp(ParserErrors.EXPECTING_FOR_LIST_START,
                                   reader.getCurrentIndex());
            reader.getNextToken();
            return reader.last() == ']';
        } catch (ParsingException e) {
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

    public JsValue nullOrArraySuchThat(final JsonReader<?> reader,
                                       final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   arraySuchThat(reader,
                                 fn
                   );
        } catch (ParsingException e) {
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
        }
    }

    public JsArray arraySuchThat(final JsonReader<?> reader,
                                 final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) {

        final JsArray array = array(reader);
        final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(array);
        if (!result.isPresent()) return array;
        throw dslExc2MyExp(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                           reader.getCurrentIndex());
    }

    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final Supplier<JsValue> f,
                              final int min,
                              final int max
    ) {
        try {
            if (ifIsEmptyArray(reader)) {
                if (min > 0)
                    throw dslExc2MyExp(ParserErrors.EMPTY_ARRAY.apply(min),
                                       reader.getCurrentIndex());
                return EMPTY;
            }

            JsArray buffer = EMPTY.append(f.get());
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(f.get());
                if (buffer.size() > max)
                    throw dslExc2MyExp(ParserErrors.TOO_LONG_ARRAY.apply(max),
                                       reader.getCurrentIndex());
            }
            if (buffer.size() < min)
                throw dslExc2MyExp(ParserErrors.TOO_SHORT_ARRAY.apply(min),
                                   reader.getCurrentIndex());

            reader.checkArrayEnd();
            return buffer;
        } catch (ParsingException e) {
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());

        }
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final Supplier<JsValue> fn,
                                    final int min,
                                    final int max
    ) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   arrayEachSuchThat(reader,
                                     fn,
                                     min,
                                     max);
        } catch (ParsingException e) {
            throw dslExc2MyExp(e.getMessage(),
                               reader.getCurrentIndex());
        }
    }


}
