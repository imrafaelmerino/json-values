package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import jsonvalues.JsObj;

final class JsObjSpecWithRequiredKeysParser extends JsObjSpecParser {
    private final Vector<String> required;


    JsObjSpecWithRequiredKeysParser(final Vector<String> required,
                                    final Map<String, JsSpecParser> parsers,
                                    final boolean strict
                                   ) {
        super(strict,
              parsers);
        this.required = required;
    }

    @Override
    JsObj value(final JsonReader<?> reader){
        try {
            final JsObj            obj      = super.value(reader);
            final Iterator<String> iterator = required.iterator();
            while (iterator.hasNext()) {
                final String key = iterator.next();
                if (!obj.containsKey(key))
                    throw reader.newParseError("Required key not found: " + key);
            }
            return obj;
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }


}
