package com.dslplatform.json;

import jsonvalues.JsObj;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

final class JsObjSpecWithRequiredKeysParser extends JsObjSpecParser {
    private final List<String> required;


    JsObjSpecWithRequiredKeysParser(List<String> required,
                                    Map<String, JsSpecParser> parsers,
                                    boolean strict,
                                    Predicate<JsObj> predicate
    ) {
        super(strict,
              parsers,
              predicate);
        this.required = required;
    }


    @Override
    JsObj value(JsonReader<?> reader) throws IOException {
        JsObj obj = super.value(reader);
        for (String key : required) {
            if (!obj.containsKey(key))
                throw new JsParserException(ParserErrors.REQUIRED_KEY_NOT_FOUND.apply(key),
                                            reader.getCurrentIndex());
        }
        return obj;

    }


}
