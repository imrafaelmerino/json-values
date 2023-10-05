package jsonvalues.spec;

import jsonvalues.JsParserException;


abstract class AbstractJsObjReader extends AbstractReader {

    protected boolean isEmptyObj(final JsReader reader) throws JsParserException {

        if (reader.last() != '{')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_START,
                                             reader.getPositionInStream()
                                            );
        return reader.readNextToken() == '}';
    }

}
