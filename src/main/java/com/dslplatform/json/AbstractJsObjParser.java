package com.dslplatform.json;

abstract class AbstractJsObjParser extends AbstractParser {

    protected boolean isEmptyObj(final JsonReader<?> reader) {
        try {
            if (reader.last() != '{')
                throw reader.newParseError(ParserErrors.EXPECTING_FOR_MAP_START,
                                           reader.getCurrentIndex());
            return reader.getNextToken() == '}';
        } catch (Exception e) {
            throw new JsParserException(e.getMessage());
        }
    }

}
