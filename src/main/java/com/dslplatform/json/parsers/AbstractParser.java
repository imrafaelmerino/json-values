package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

abstract class AbstractParser {
    static final JsObj EMPTY_OBJ = JsObj.empty();

    JsValue nullOrValue(final JsonReader<?> reader){
        try {
            return reader.wasNull() ? JsNull.NULL : value(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    abstract JsValue value(final JsonReader<?> reader) ;


}
