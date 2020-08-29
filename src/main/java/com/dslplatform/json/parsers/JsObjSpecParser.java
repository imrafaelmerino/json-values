package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import io.vavr.collection.Map;
import jsonvalues.JsObj;

import java.io.IOException;

class JsObjSpecParser extends AbstractJsObjParser {
    private static final JsValueParser valueParser = JsParsers.PARSERS.valueParser;
    private static final JsSpecParser defaultParser = valueParser::value;
    protected final boolean strict;
    private final Map<String, JsSpecParser> parsers;

    JsObjSpecParser(boolean strict,
                    final Map<String, JsSpecParser> parsers
                   ) {
        this.strict = strict;
        this.parsers = parsers;
    }

    @Override
    JsObj value(final JsonReader<?> reader){
        try {
            if (isEmptyObj(reader)) return EMPTY_OBJ;
            String key = reader.readKey();
            throwErrorIfStrictAndKeyMissing(reader,
                                            key
                                           );
            JsObj obj = EMPTY_OBJ.set(key,
                                      parsers.getOrElse(key,
                                                        defaultParser
                                                       )
                                             .parse(reader)
                                     );
            byte nextToken;
            while ((nextToken = reader.getNextToken()) == ',') {
                reader.getNextToken();
                key = reader.readKey();
                throwErrorIfStrictAndKeyMissing(reader,
                                                key
                                               );
                obj = obj.set(key,
                              parsers.getOrElse(key,
                                                defaultParser
                                               )
                                     .parse(reader)
                             );

            }
            if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
            return obj;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    private void throwErrorIfStrictAndKeyMissing(final JsonReader<?> reader,
                                                 final String key
                                                ) throws com.dslplatform.json.ParsingException {
        if (strict && !parsers.containsKey(key)) {
            throw reader.newParseError("There is no spec defined for the key " + key);
        }
    }


}
