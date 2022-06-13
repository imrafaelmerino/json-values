package com.dslplatform.json;

import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

abstract class AbstractParser {
    static final JsObj EMPTY_OBJ = JsObj.empty();

    JsValue nullOrValue(final JsonReader<?> reader) {
        try {
            return reader.wasNull() ?
                   JsNull.NULL :
                   value(reader);
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } catch (JsParserException e) {
            throw e;
        } catch (Exception e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

    abstract JsValue value(final JsonReader<?> reader);


}
