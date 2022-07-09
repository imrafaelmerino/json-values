package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsObjParser extends AbstractJsObjParser {

    private final AbstractParser valueDeserializer;

    JsObjParser(final AbstractParser valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    JsObj valueSuchThat(final JsonReader<?> reader,
                        final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) throws IOException {
        final JsObj value = value(reader);
        final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
        if (!result.isPresent()) return value;
        throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                    reader.getCurrentIndex());
    }

    @Override
    public JsObj value(final JsonReader<?> reader) throws IOException {
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
            throw new JsParserException(ParserErrors.EXPECTING_FOR_MAP_END,
                                        reader.getCurrentIndex());
        return map;
    }


}
