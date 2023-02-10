package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsParserException;

import java.util.Optional;
import java.util.function.Function;

final class JsObjReader extends AbstractJsObjReader {

    private final AbstractReader valueDeserializer;

    JsObjReader(final AbstractReader valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    JsObj valueSuchThat(final JsReader reader,
                        final Function<JsObj, Optional<JsError>> fn
                       ) throws JsParserException {
        final JsObj value = value(reader);
        final Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return value;
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

    @Override
    public JsObj value(final JsReader reader) throws JsParserException {
        if (isEmptyObj(reader)) return EMPTY_OBJ;

        String key = reader.readKey();
        JsObj map = EMPTY_OBJ.set(key,
                                  valueDeserializer.value(reader)
                                 );
        byte nextToken;
        while ((nextToken = reader.readNextToken()) == ',') {
            reader.readNextToken();
            key = reader.readKey();
            map = map.set(key,
                          valueDeserializer.value(reader)
                         );
        }
        if (nextToken != '}')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_END,
                                             reader.getPositionInStream()
                                            );
        return map;
    }



}
