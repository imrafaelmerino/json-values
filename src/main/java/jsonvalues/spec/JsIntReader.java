package jsonvalues.spec;

import java.util.function.IntFunction;
import jsonvalues.JsInt;

final class JsIntReader extends AbstractReader {

  @Override
  JsInt value(final DslJsReader reader) throws JsParserException {
    return JsInt.of(NumberConverter.deserializeInt(reader));
  }

  JsInt valueSuchThat(final DslJsReader reader,
                      final IntFunction<JsError> fn
                     ) throws JsParserException {
    int value = NumberConverter.deserializeInt(reader);
    JsError result = fn.apply(value);
    if (result == null) {
      return JsInt.of(value);
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }


}
