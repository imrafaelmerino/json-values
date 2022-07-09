package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.*;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

final class JsNumberParser extends AbstractParser {

    JsNumber valueSuchThat(final JsonReader<?> reader,
                           final Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) throws IOException {
        final JsNumber value = value(reader);
        final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
        if (!result.isPresent()) return value;
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
