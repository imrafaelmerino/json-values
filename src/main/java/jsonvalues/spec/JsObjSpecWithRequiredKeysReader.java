package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsParserException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

final class JsObjSpecWithRequiredKeysReader extends JsObjSpecReader {
    private final List<String> required;


    JsObjSpecWithRequiredKeysReader(List<String> required,
                                    Map<String, JsSpecParser> parsers,
                                    boolean strict,
                                    Predicate<JsObj> predicate
                                   ) {
        super(strict,
              parsers,
              predicate
             );
        this.required = required;
    }


    @Override
    JsObj value(JsonReader reader) throws IOException {
        JsObj obj = super.value(reader);
        for (String key : required) {
            if (!obj.containsKey(key))
                throw JsParserException.create(ParserErrors.REQUIRED_KEY_NOT_FOUND.apply(key),
                                               reader.getCurrentIndex(),
                                               false
                                              );
        }
        return obj;

    }


}
