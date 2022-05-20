package com.dslplatform.json;

import jsonvalues.JsLong;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsLongParser extends AbstractParser {

    @Override
    JsLong value(final JsonReader<?> reader) {
        try {
            return JsLong.of(MyNumberConverter.deserializeLong(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsLong valueSuchThat(final JsonReader<?> reader,
                         final LongFunction<Optional<JsError>> fn
    ) {
        try {
            long value = MyNumberConverter.deserializeLong(reader);
            Optional<JsError> result = fn.apply(value);
            if (!result.isPresent()) return JsLong.of(value);
            throw reader.newParseError(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
