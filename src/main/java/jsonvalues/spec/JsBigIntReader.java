package jsonvalues.spec;

import jsonvalues.JsBigInt;
import jsonvalues.JsParserException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

final class JsBigIntReader extends AbstractReader {
    @Override
    JsBigInt value(final JsReader reader) throws IOException {
        try {

            return JsBigInt.of(NumberConverter.deserializeDecimal(reader)
                                              .toBigIntegerExact());

        } catch (ArithmeticException e) {
            throw JsParserException.reasonAt(ParserErrors.INTEGRAL_NUMBER_EXPECTED,
                                             reader.getPositionInStream()
                                            );
        }

    }

    JsBigInt valueSuchThat(final JsReader reader,
                           final Function<BigInteger, Optional<JsError>> fn
                          ) throws IOException {
        try {
            BigDecimal bigDecimal = NumberConverter.deserializeDecimal(reader);
            final BigInteger value = bigDecimal.toBigIntegerExact();
            final Optional<JsError> result = fn.apply(value);
            if (result.isEmpty()) return JsBigInt.of(value);

            throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                             reader.getPositionInStream()
                                            );
        } catch (ArithmeticException e) {
            throw JsParserException.reasonAt(ParserErrors.BIG_INTEGER_WITH_FRACTIONAL_PART,
                                             reader.getPositionInStream()
                                            );
        }

    }


}
