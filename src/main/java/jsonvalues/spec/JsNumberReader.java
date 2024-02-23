package jsonvalues.spec;

import java.math.BigDecimal;
import java.util.function.Function;
import jsonvalues.JsBigDec;
import jsonvalues.JsDouble;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNumber;

final class JsNumberReader extends AbstractReader {

  JsNumber valueSuchThat(final JsReader reader,
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
  JsNumber value(final JsReader reader) throws JsParserException {
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
    }
    return JsBigDec.of(((BigDecimal) number));
  }


}
