package com.dslplatform.json;


import jsonvalues.JsObj;

import java.util.List;
import java.util.Map;

final class JsObjSpecWithRequiredKeysParser extends JsObjSpecParser {
    private final List<String> required;


    JsObjSpecWithRequiredKeysParser(final List<String> required,
                                    final Map<String, JsSpecParser> parsers,
                                    final boolean strict
    ) {
        super(strict,
              parsers);
        this.required = required;
    }

    @Override
    JsObj value(final JsonReader<?> reader) {
        try {
            final JsObj obj = super.value(reader);
            for (String key : required) {
                if (!obj.containsKey(key))
                    throw reader.newParseError("Required key not found: " + key);
            }
            return obj;
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }


}
