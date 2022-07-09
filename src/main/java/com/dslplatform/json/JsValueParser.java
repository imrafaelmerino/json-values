package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsBool;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsValueParser extends AbstractParser {
    private JsObjParser objDeserializer;
    private JsArrayOfValueParser arrayDeserializer;
    private JsNumberParser numberDeserializer;

    void setNumberDeserializer(final JsNumberParser numberDeserializer) {
        this.numberDeserializer = numberDeserializer;
    }

    void setObjDeserializer(final JsObjParser objDeserializer) {
        this.objDeserializer = objDeserializer;
    }

    void setArrayDeserializer(final JsArrayOfValueParser arrayDeserializer) {
        this.arrayDeserializer = arrayDeserializer;
    }

    JsValue valueSuchThat(final JsonReader<?> reader,
                          final Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> fn

    ) throws IOException {
        JsValue value = value(reader);
        Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
        if (!result.isPresent()) return value;
        throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                    reader.getCurrentIndex());
    }

    @Override
    JsValue value(final JsonReader<?> reader) throws IOException {
        switch (reader.last()) {
            case 't':
                if (!reader.wasTrue()) {
                    throw new JsParserException(ParserErrors.EXPECTING_TRUE,
                                                reader.getCurrentIndex()
                    );
                }
                return JsBool.TRUE;
            case 'f':
                if (!reader.wasFalse()) {
                    throw new JsParserException(ParserErrors.EXPECTING_FALSE,
                                                reader.getCurrentIndex());
                }
                return JsBool.FALSE;
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
