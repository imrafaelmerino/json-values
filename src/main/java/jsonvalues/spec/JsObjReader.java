package jsonvalues.spec;

import java.util.function.Consumer;
import java.util.function.Function;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

final class JsObjReader extends AbstractJsObjReader {

  private final AbstractReader valueDeserializer;

  JsObjReader(final AbstractReader valueDeserializer) {
    this.valueDeserializer = valueDeserializer;
  }

  JsObj valueSuchThat(final DslJsReader reader,
                      final Function<JsObj, JsError> fn
                     ) throws JsParserException {
    JsObj value = value(reader);
    JsError result = fn.apply(value);
    if (result == null) {
      return value;
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }

  @Override
  public JsObj value(final DslJsReader reader) throws JsParserException {
    if (isEmptyObj(reader)) {
      return EMPTY_OBJ;
    }

    String key = reader.readKey();
    JsObj map = EMPTY_OBJ.set(key,
                              valueDeserializer.value(reader)
                             );
    byte nextToken;
    while ((nextToken = reader.readNextToken()) == ',') {
      reader.readNextToken();
      key = reader.readKey();
      map = map.set(key,
                    valueDeserializer.value(reader)
                   );
    }
    if (nextToken != '}') {
      throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_END.formatted(((char) nextToken)),
                                       reader.getPositionInStream()
                                      );
    }
    return map;
  }

  JsValue eachEntrySuchThat(final DslJsReader reader,
                            final Consumer<JsValue> validateEach,
                            final boolean nullable
                           ) throws JsParserException {
    if (reader.wasNull()) {
      if (nullable) {
        return JsNull.NULL;
      } else {
        throw JsParserException.reasonAt(ParserErrors.INVALID_NULL,
                                         reader.getPositionInStream()
                                        );
      }
    }

    if (isEmptyObj(reader)) {
      return EMPTY_OBJ;
    }

    var key = reader.readKey();
    var value = valueDeserializer.value(reader);
    validateEach.accept(value);
    var map = EMPTY_OBJ.set(key,
                            value
                           );
    byte nextToken;
    while ((nextToken = reader.readNextToken()) == ',') {
      reader.readNextToken();
      key = reader.readKey();
      value = valueDeserializer.value(reader);
      validateEach.accept(value);
      map = map.set(key,
                    value
                   );
    }
    if (nextToken != '}') {
      throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_END.formatted(((char) nextToken)),
                                       reader.getPositionInStream()
                                      );
    }
    return map;
  }


}
