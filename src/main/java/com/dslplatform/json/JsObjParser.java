package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.util.Optional;
import java.util.function.Function;

final class JsObjParser extends AbstractJsObjParser {

    private final JsValueParser valueDeserializer;

    JsObjParser(final JsValueParser valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    JsObj valueSuchThat(final JsonReader<?> reader,
                        final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) {
        try {
            final JsObj value = value(reader);
            final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
            if (!result.isPresent()) return value;
            throw reader.newParseError(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }

    }

    @Override
    public JsObj value(final JsonReader<?> reader) {
        try {
            if (isEmptyObj(reader)) return EMPTY_OBJ;

            String key = reader.readKey();
            JsObj map = EMPTY_OBJ.set(key,
                                      valueDeserializer.value(reader)
            );
            byte nextToken;
            while ((nextToken = reader.getNextToken()) == ',') {
                reader.getNextToken();
                key = reader.readKey();
                map = map.set(key,
                              valueDeserializer.value(reader));
            }
            if (nextToken != '}')
                throw reader.newParseError(ParserErrors.EXPECTING_FOR_MAP_END,
                                           reader.getCurrentIndex());
            return map;
        } catch (Exception e) {
            throw new JsParserException(e.getMessage());

        }
    }

}
