package jsonvalues.spec;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsDouble;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNumber;

final class JsNumberReader extends AbstractReader {

  JsNumber valueSuchThat(final DslJsReader reader,
                         final Function<JsNumber, JsError> fn
                        ) throws JsParserException {
    final JsNumber value = value(reader);
    final JsError result = fn.apply(value);
    if (result == null) {
      return value;
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }

  @Override
  JsNumber value(final DslJsReader reader) throws JsParserException {
    Number number = NumberConverter.deserializeNumber(reader);
    if (number instanceof Double) {
      return JsDouble.of(((double) number));
    }
    if (number instanceof Long) {
      long n = (long) number;
      try {
        return JsInt.of(Math.toIntExact(n));
      } catch (ArithmeticException e) {
        return JsLong.of(n);
      }
    } else if (number instanceof BigInteger) {
      return JsBigInt.of((BigInteger) number);
    }
    return JsBigDec.of(((BigDecimal) number));
  }


}
