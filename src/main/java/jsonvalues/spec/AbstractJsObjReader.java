package jsonvalues.spec;

import jsonvalues.JsParserException;

import java.io.IOException;

abstract class AbstractJsObjReader extends AbstractReader {

    protected boolean isEmptyObj(final JsonReader reader) throws IOException {

        if (reader.last() != '{')
            throw JsParserException.create(ParserErrors.EXPECTING_FOR_MAP_START,
                                           reader.getCurrentIndex(),
                                           false
                                          );
        return reader.getNextToken() == '}';
    }

}
