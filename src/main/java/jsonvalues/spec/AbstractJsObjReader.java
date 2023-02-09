package jsonvalues.spec;

import jsonvalues.JsParserException;

import java.io.IOException;

abstract class AbstractJsObjReader extends AbstractReader {

    protected boolean isEmptyObj(final JsReader reader) throws IOException {

        if (reader.last() != '{')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_START,
                                             reader.getPositionInStream()
                                            );
        return reader.getNextToken() == '}';
    }

}
