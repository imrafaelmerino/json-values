package com.dslplatform.json;


import jsonvalues.JsObj;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

final class JsObjSpecWithRequiredKeysParser extends JsObjSpecParser {
    private final List<String> required;



    JsObjSpecWithRequiredKeysParser(final List<String> required,
                                    final Map<String, JsSpecParser> parsers,
                                    final boolean strict,
                                    Predicate<JsObj> predicate
    ) {
        super(strict,
              parsers,
              predicate);
        this.required = required;
    }


    @Override
    JsObj value(final JsonReader<?> reader) {
        try {
            final JsObj obj = super.value(reader);
            for (String key : required) {
                if (!obj.containsKey(key))
                    throw reader.newParseError(ParserErrors.REQUIRED_KEY_NOT_FOUND.apply(key),
                                               reader.getCurrentIndex());
            }
            return obj;
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }


}
