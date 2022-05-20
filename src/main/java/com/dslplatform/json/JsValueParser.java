package com.dslplatform.json;

import jsonvalues.JsBool;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

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
                          final Function<JsValue, Optional<JsError>> fn

    ) {
        try {
            JsValue value = value(reader);
            Optional<JsError> result = fn.apply(value);
            if (!result.isPresent()) return value;
            throw reader.newParseError(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }

    }

    @Override
    JsValue value(final JsonReader<?> reader) {
        try {
            switch (reader.last()) {
                case 't':
                    if (!reader.wasTrue()) {
                        throw new JsParserException(reader.newParseErrorAt("Expecting 'true' for true constant",
                                                                           0
                        ));
                    }
                    return JsBool.TRUE;
                case 'f':
                    if (!reader.wasFalse()) {
                        throw new JsParserException(reader.newParseErrorAt("Expecting 'false' for false constant",
                                                                           0
                        ));
                    }
                    return JsBool.FALSE;
                case '"':
                    return JsStr.of(reader.readString());
                case '{':
                    return objDeserializer.value(reader);
                case '[':
                    return arrayDeserializer.array(reader);
                default:
                    return numberDeserializer.value(reader);
            }
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

}
