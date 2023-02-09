package jsonvalues.spec;

import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.io.IOException;


abstract class AbstractReader {
    static final JsObj EMPTY_OBJ = JsObj.empty();

    JsValue nullOrValue(final JsReader reader) throws IOException {

        return reader.wasNull() ?
               JsNull.NULL :
               value(reader);
    }

    abstract JsValue value(final JsReader reader) throws IOException;

}
