package jsonvalues.spec;

import java.util.function.DoubleFunction;
import jsonvalues.JsDouble;

final class JsDoubleReader extends AbstractReader {

  @Override
  JsDouble value(final DslJsReader reader) throws JsParserException {
    return JsDouble.of(NumberConverter.deserializeDouble(reader));
  }

  JsDouble valueSuchThat(final DslJsReader reader,
                         final DoubleFunction<JsError> fn
                        ) throws JsParserException {
    double value = NumberConverter.deserializeDouble(reader);
    JsError result = fn.apply(value);
    if (result == null) {
      return JsDouble.of(value);
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }

}
