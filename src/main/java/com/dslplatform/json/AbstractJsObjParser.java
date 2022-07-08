package com.dslplatform.json;

import java.io.IOException;

abstract class AbstractJsObjParser extends AbstractParser {

    protected boolean isEmptyObj(final JsonReader<?> reader) {
        try {
            if (reader.last() != '{')
                throw new JsParserException(ParserErrors.EXPECTING_FOR_MAP_START,
                                            reader.getCurrentIndex());
            return reader.getNextToken() == '}';
        } catch (IOException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }

}
