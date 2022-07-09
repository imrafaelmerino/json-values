package com.dslplatform.json;

import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.io.IOException;


abstract class AbstractParser {
    static final JsObj EMPTY_OBJ = JsObj.empty();

    JsValue nullOrValue(final JsonReader<?> reader) throws IOException {

        return reader.wasNull() ?
               JsNull.NULL :
               value(reader);
    }

    abstract JsValue value(final JsonReader<?> reader) throws IOException;

}
