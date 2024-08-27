package jsonvalues.spec;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import jsonvalues.JsBigInt;

final class JsBigIntReader extends AbstractReader {

  @Override
  JsBigInt value(final DslJsReader reader) throws JsParserException {
    try {

      return JsBigInt.of(NumberConverter.deserializeDecimal(reader)
                                        .toBigIntegerExact());

    } catch (ArithmeticException e) {
      throw JsParserException.reasonAt(ParserErrors.INTEGRAL_NUMBER_EXPECTED,
                                       reader.getPositionInStream()
                                      );
    }

  }

  JsBigInt valueSuchThat(final DslJsReader reader,
                         final Function<BigInteger, JsError> fn
                        ) throws JsParserException {
    try {
      BigDecimal bigDecimal = NumberConverter.deserializeDecimal(reader);
      BigInteger value = bigDecimal.toBigIntegerExact();
      JsError result = fn.apply(value);
      if (result == null) {
        return JsBigInt.of(value);
      }

      throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                       reader.getPositionInStream()
                                      );
    } catch (ArithmeticException e) {
      throw JsParserException.reasonAt(ParserErrors.BIG_INTEGER_WITH_FRACTIONAL_PART,
                                       reader.getPositionInStream()
                                      );
    }

  }


}
