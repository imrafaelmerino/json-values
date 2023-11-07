package jsonvalues.spec;

import jsonvalues.JsObj;

import java.util.Objects;

class JsMapOfSpecReader extends AbstractJsObjReader {
    private final JsParser parser;


    JsMapOfSpecReader(JsParser parser) {
        this.parser = Objects.requireNonNull(parser);
    }

    @Override
    JsObj value(final JsReader reader) throws JsParserException {
        if (isEmptyObj(reader)) return EMPTY_OBJ;
        String key = reader.readKey();

        JsObj obj = EMPTY_OBJ.set(key,
                                  parser.parse(reader)
                                 );
        byte nextToken;
        while ((nextToken = reader.readNextToken()) == ',') {
            reader.readNextToken();
            key = reader.readKey();

            obj = obj.set(key,
                          parser.parse(reader)
                         );

        }
        if (nextToken != '}')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_END.formatted(((char) reader.last())),
                                             reader.getPositionInStream()
                                            );

        return obj;

    }


}
