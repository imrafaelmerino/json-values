package jsonvalues.spec;


import java.util.function.Function;
import jsonvalues.JsBool;
import jsonvalues.JsStr;
import jsonvalues.JsValue;


class JsValueReader extends AbstractReader {

  private JsObjReader objDeserializer;
  private JsArrayOfValueReader arrayDeserializer;
  private JsNumberReader numberDeserializer;

  void setNumberDeserializer(JsNumberReader numberDeserializer) {
    this.numberDeserializer = numberDeserializer;
  }

  void setObjDeserializer(JsObjReader objDeserializer) {
    this.objDeserializer = objDeserializer;
  }

  void setArrayDeserializer(JsArrayOfValueReader arrayDeserializer) {
    this.arrayDeserializer = arrayDeserializer;
  }

  JsValue valueSuchThat(DslJsReader reader,
                        Function<JsValue, JsError> fn

                       ) throws JsParserException {
    JsValue value = value(reader);
    JsError result = fn.apply(value);
    if (result == null) {
      return value;
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }

  @Override
  @SuppressWarnings("FallThrough")
  JsValue value(DslJsReader reader) throws JsParserException {
    return switch (reader.last()) {
      case 't' -> {
        if (reader.wasTrue()) {
          yield JsBool.TRUE;
        }
        throw JsParserException.reasonAt("true was expected",
                                         reader.getCurrentIndex());
      }
      case 'f' -> {
        if (reader.wasFalse()) {
          yield JsBool.FALSE;
        }
        throw JsParserException.reasonAt("false was expected",
                                         reader.getCurrentIndex());
      }
      case '"' -> JsStr.of(reader.readString());
      case '{' -> objDeserializer.value(reader);
      case '[' -> arrayDeserializer.nullOrValue(reader);
      default -> numberDeserializer.nullOrValue(reader);
    };
  }


}
