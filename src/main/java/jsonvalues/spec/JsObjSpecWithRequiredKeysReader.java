package jsonvalues.spec;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import jsonvalues.JsObj;

final class JsObjSpecWithRequiredKeysReader extends JsObjSpecReader {

  private final List<String> required;


  JsObjSpecWithRequiredKeysReader(List<String> required,
                                  Map<String, JsParser> parsers,
                                  boolean strict,
                                  Predicate<JsObj> predicate,
                                  MetaData metaData
                                 ) {
    super(strict,
          parsers,
          predicate,
          metaData
         );
    this.required = required;
  }


  @Override
  JsObj value(DslJsReader reader) throws JsParserException {
    JsObj obj = super.value(reader);
    for (String key : required) {
      if (!obj.containsKey(key)) {
        throw JsParserException.reasonAt(ParserErrors.REQUIRED_KEY_NOT_FOUND.apply(key),
                                         reader.getPositionInStream()
                                        );
      }
    }
    return obj;

  }


}
