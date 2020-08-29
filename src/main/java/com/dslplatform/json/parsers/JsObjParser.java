package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsObj;
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
                       ){
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
    public JsObj value(final JsonReader<?> reader){
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
                              valueDeserializer.value(reader)
                             );
            }
            if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
            return map;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }

}
