package jsonvalues.spec;

import jsonvalues.JsParserException;


abstract class AbstractJsObjReader extends AbstractReader {

    protected boolean isEmptyObj(final JsReader reader) throws JsParserException {


        byte last = reader.last();
        if (last != '{')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_START+" but get "+ ((char) last),
                                             reader.getPositionInStream()
                                            );
        return reader.readNextToken() == '}';
    }

}
