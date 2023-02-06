package com.dslplatform.json;


import jsonvalues.JsBool;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;


class JsValueParser extends AbstractParser {
    private JsObjParser objDeserializer;
    private JsArrayOfValueParser arrayDeserializer;
    private JsNumberParser numberDeserializer;

    void setNumberDeserializer(JsNumberParser numberDeserializer) {
        this.numberDeserializer = numberDeserializer;
    }

    void setObjDeserializer(JsObjParser objDeserializer) {
        this.objDeserializer = objDeserializer;
    }

    void setArrayDeserializer(JsArrayOfValueParser arrayDeserializer) {
        this.arrayDeserializer = arrayDeserializer;
    }

    JsValue valueSuchThat(JsonReader<?> reader,
                          Function<JsValue, Optional<JsError>> fn

    ) throws IOException {
        JsValue value = value(reader);
        Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return value;
        throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                    reader.getCurrentIndex());
    }

    @Override
    @SuppressWarnings("FallThrough")
    JsValue value(JsonReader<?> reader) throws IOException {
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
                return arrayDeserializer.value(reader);
            default:
                return numberDeserializer.value(reader);
        }
    }


}
