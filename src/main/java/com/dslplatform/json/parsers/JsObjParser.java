package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import io.vavr.collection.HashMap;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsObjParser extends AbstractJsObjParser {

    private final JsValueParser valueDeserializer;

    JsObjParser(final JsValueParser valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    JsObj valueSuchThat(final JsonReader<?> reader,
                        final Function<JsObj, Optional<Error>> fn
                       ) throws JsParserException {
        try {
            final JsObj           value  = value(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return value;
            throw reader.newParseError(result.toString());
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }

    }

    @Override
    public JsObj value(final JsonReader<?> reader) throws JsParserException {
        try {
            if (isEmptyObj(reader)) return EMPTY_OBJ;

            String key = reader.readKey();
            HashMap<String, JsValue> map = EMPTY_MAP.put(key,
                                                         valueDeserializer.value(reader
                                                                                )
                                                        );
            byte nextToken;
            while ((nextToken = reader.getNextToken()) == ',') {
                reader.getNextToken();
                key = reader.readKey();
                map = map.put(key,
                              valueDeserializer.value(reader
                                                     )
                             );
            }
            if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
            return new JsObj(map);
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }

}
