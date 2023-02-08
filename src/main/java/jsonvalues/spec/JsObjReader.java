package jsonvalues.spec;

import jsonvalues.JsObj;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsObjReader extends AbstractJsObjReader {

    private final AbstractReader valueDeserializer;

    JsObjReader(final AbstractReader valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    JsObj valueSuchThat(final JsonReader reader,
                        final Function<JsObj, Optional<JsError>> fn
                       ) throws IOException {
        final JsObj value = value(reader);
        final Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return value;
        throw JsParserException.create(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex(),
                                       false
                                      );
    }

    @Override
    public JsObj value(final JsonReader reader) throws IOException {
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
        if (nextToken != '}')
            throw JsParserException.create(ParserErrors.EXPECTING_FOR_MAP_END,
                                           reader.getCurrentIndex(),
                                           false
                                          );
        return map;
    }



}
