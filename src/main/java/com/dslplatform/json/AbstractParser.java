package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

abstract class AbstractParser {
    static final JsObj EMPTY_OBJ = JsObj.empty();

    JsValue nullOrValue(final JsonReader<?> reader) throws IOException {

        return reader.wasNull() ?
               JsNull.NULL :
               value(reader);
    }

    abstract JsValue value(final JsonReader<?> reader) throws IOException;

}
