package com.dslplatform.json;

import jsonvalues.JsInt;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntParser extends AbstractParser {
    @Override
    JsInt value(final JsonReader<?> reader) {
        try {
            return JsInt.of(MyNumberConverter.deserializeInt(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsInt valueSuchThat(final JsonReader<?> reader,
                        final IntFunction<Optional<JsError>> fn
    ) {
        try {
            int value = MyNumberConverter.deserializeInt(reader);
            Optional<JsError> result = fn.apply(value);
            if (!result.isPresent()) return JsInt.of(value);
            throw reader.newParseError(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
