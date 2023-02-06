package com.dslplatform.json;

import jsonvalues.JsBigInt;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

final class JsBigIntParser extends AbstractParser {
    @Override
    JsBigInt value(final JsonReader<?> reader) throws IOException {
        try {

            return JsBigInt.of(MyNumberConverter.deserializeDecimal(reader)
                                                .toBigIntegerExact());

        } catch (ArithmeticException e) {
            throw new JsParserException(ParserErrors.INTEGRAL_NUMBER_EXPECTED,
                                        reader.getCurrentIndex());
        }

    }

    JsBigInt valueSuchThat(final JsonReader<?> reader,
                           final Function<BigInteger, Optional<JsError>> fn
    ) throws IOException {
        try {
            BigDecimal bigDecimal = MyNumberConverter.deserializeDecimal(reader);
            final BigInteger value = bigDecimal.toBigIntegerExact();
            final Optional<JsError> result = fn.apply(value);
            if (result.isEmpty()) return JsBigInt.of(value);

            throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                        reader.getCurrentIndex());
        } catch (ArithmeticException e) {
            throw new JsParserException(ParserErrors.BIG_INTEGER_WITH_FRACTIONAL_PART,
                                        reader.getCurrentIndex());
        }

    }


}
