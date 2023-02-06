package com.dslplatform.json;

import jsonvalues.JsStr;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsStrParser extends AbstractParser {
    @Override
    JsStr value(final JsonReader<?> reader) throws IOException {
        return JsStr.of(StringConverter.deserialize(reader));
    }


    JsStr valueSuchThat(final JsonReader<?> reader,
                        final Function<String, Optional<JsError>> fn
    ) throws IOException {
            String value = StringConverter.deserialize(reader);
            Optional<JsError> result = fn.apply(value);
            if (result.isEmpty()) return JsStr.of(value);
            throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                        reader.getCurrentIndex());
    }

}
