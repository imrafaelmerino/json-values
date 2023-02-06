package com.dslplatform.json;

import jsonvalues.*;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

final class JsNumberParser extends AbstractParser {

    JsNumber valueSuchThat(final JsonReader<?> reader,
                           final Function<JsNumber, Optional<JsError>> fn
    ) throws IOException {
        final JsNumber value = value(reader);
        final Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return value;
        throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                    reader.getCurrentIndex());
    }

    @Override
    JsNumber value(final JsonReader<?> reader) throws IOException {
        Number number = MyNumberConverter.deserializeNumber(reader);
        if (number instanceof Double) return JsDouble.of(((double) number));
        if (number instanceof Long) {
            long n = (long) number;
            try {
                return JsInt.of(Math.toIntExact(n));
            } catch (ArithmeticException e) {
                return JsLong.of(n);
            }
        }
        return JsBigDec.of(((BigDecimal) number));
    }


}
