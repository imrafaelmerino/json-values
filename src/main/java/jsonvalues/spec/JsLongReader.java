package jsonvalues.spec;

import java.util.function.LongFunction;
import jsonvalues.JsLong;

final class JsLongReader extends AbstractReader {

  @Override
  JsLong value(final DslJsReader reader) throws JsParserException {
    return JsLong.of(NumberConverter.deserializeLong(reader));
  }

  JsLong valueSuchThat(final DslJsReader reader,
                       final LongFunction<JsError> fn
                      ) throws JsParserException {
    long value = NumberConverter.deserializeLong(reader);
    JsError result = fn.apply(value);
    if (result == null) {
      return JsLong.of(value);
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }

}
