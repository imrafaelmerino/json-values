package jsonvalues.spec;


import jsonvalues.JsBool;
import jsonvalues.JsParserException;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;


class JsValueReader extends AbstractReader {
    private JsObjReader objDeserializer;
    private JsArrayOfValueReader arrayDeserializer;
    private JsNumberReader numberDeserializer;

    void setNumberDeserializer(JsNumberReader numberDeserializer) {
        this.numberDeserializer = numberDeserializer;
    }

    void setObjDeserializer(JsObjReader objDeserializer) {
        this.objDeserializer = objDeserializer;
    }

    void setArrayDeserializer(JsArrayOfValueReader arrayDeserializer) {
        this.arrayDeserializer = arrayDeserializer;
    }

    JsValue valueSuchThat(JsReader reader,
                          Function<JsValue, Optional<JsError>> fn

                         ) throws IOException {
        JsValue value = value(reader);
        Optional<JsError> result = fn.apply(value);
        if (!result.isPresent()) return value;
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

    @Override
    @SuppressWarnings("FallThrough")
    JsValue value(JsReader reader) throws IOException {
        switch (reader.last()) {
            case 't':
                if (reader.wasTrue()) return JsBool.TRUE;

            case 'f':
                if (reader.wasFalse()) return JsBool.FALSE;
            case '"':
                return JsStr.of(reader.readString());
            case '{':
                return objDeserializer.value(reader);
            case '[':
                return arrayDeserializer.nullOrValue(reader);
            default:
                return numberDeserializer.nullOrValue(reader);
        }
    }


}
