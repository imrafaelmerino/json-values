package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsBigInt;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

final class JsBigIntParser extends AbstractParser {
    @Override
    JsBigInt value(final JsonReader<?> reader) {
        try {

            return JsBigInt.of(MyNumberConverter.deserializeDecimal(reader)
                                                .toBigIntegerExact());

        } catch (ArithmeticException | IOException e) {
            throw new JsParserException(reader.newParseError(ParserErrors.INTEGRAL_NUMBER_EXPECTED,
                                                             reader.getCurrentIndex()));
        }
    }

    JsBigInt valueSuchThat(final JsonReader<?> reader,
                           final Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) {
        try {
            BigDecimal bigDecimal = MyNumberConverter.deserializeDecimal(reader);
            final BigInteger value = bigDecimal.toBigIntegerExact();
            final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
            if (!result.isPresent()) return JsBigInt.of(value);
            throw reader.newParseError(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        }

        catch (ArithmeticException e){
            throw new JsParserException(ParserErrors.BIG_INTEGER_WITH_FRACTIONAL_PART);
        }

        catch (Exception e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
