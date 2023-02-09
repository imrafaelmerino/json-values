package jsonvalues.spec;

import jsonvalues.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

final class JsNumberReader extends AbstractReader {

    JsNumber valueSuchThat(final JsReader reader,
                           final Function<JsNumber, Optional<JsError>> fn
                          ) throws IOException {
        final JsNumber value = value(reader);
        final Optional<JsError> result = fn.apply(value);
        if (!result.isPresent()) return value;
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

    @Override
    JsNumber value(final JsReader reader) throws IOException {
        Number number = NumberConverter.deserializeNumber(reader);
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
