package jsonvalues.spec;

import java.math.BigDecimal;
import java.util.function.Function;
import jsonvalues.JsBigDec;


final class JsDecimalReader extends AbstractReader {

  @Override
  JsBigDec value(final DslJsReader reader) throws JsParserException {
    return JsBigDec.of(NumberConverter.deserializeDecimal(reader));
  }

  JsBigDec valueSuchThat(final DslJsReader reader,
                         final Function<BigDecimal, JsError> fn
                        ) throws JsParserException {
    final BigDecimal value = NumberConverter.deserializeDecimal(reader);
    final JsError result = fn.apply(value);
    if (result == null) {
      return JsBigDec.of(value);
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }


}
