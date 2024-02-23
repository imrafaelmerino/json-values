package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsStr;

final class JsStrReader extends AbstractReader {

  @Override
  JsStr value(final JsReader reader) throws JsParserException {
    return JsStr.of(reader.readString());
  }


  JsStr valueSuchThat(final JsReader reader,
                      final Function<String, JsError> fn
                     ) throws JsParserException {
    String value = reader.readString();
    JsError result = fn.apply(value);
    if (result == null) {
      return JsStr.of(value);
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }

}
