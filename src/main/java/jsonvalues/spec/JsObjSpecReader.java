package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsParserException;

import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;

class JsObjSpecReader extends AbstractJsObjReader {
    private static final JsValueReader valueParser = JsParsers.PARSERS.valueParser;
    private static final JsSpecParser defaultParser = valueParser::nullOrValue;
    protected final boolean strict;
    private final Map<String, JsSpecParser> parsers;

    protected Predicate<JsObj> predicate;

    JsObjSpecReader(boolean strict,
                    Map<String, JsSpecParser> parsers,
                    Predicate<JsObj> predicate
                   ) {
        this.strict = strict;
        this.parsers = parsers;
        this.predicate = predicate;
    }

    @Override
    JsObj value(final JsReader reader) throws IOException {
        if (isEmptyObj(reader)) return EMPTY_OBJ;
        String key = reader.readKey();
        throwErrorIfStrictAndKeyMissing(reader,
                                        key
                                       );
        JsSpecParser parser = parsers.getOrDefault(key,
                                                   defaultParser
                                                  );
        JsObj obj = EMPTY_OBJ.set(key,
                                  parser
                                          .parse(reader)
                                 );
        byte nextToken;
        while ((nextToken = reader.getNextToken()) == ',') {
            reader.getNextToken();
            key = reader.readKey();
            throwErrorIfStrictAndKeyMissing(reader,
                                            key
                                           );
            JsSpecParser keyparser = parsers.getOrDefault(key,
                                                          defaultParser
                                                         );
            obj = obj.set(key,
                          keyparser.parse(reader)
                         );

        }
        if (nextToken != '}')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_END,
                                             reader.getPositionInStream()
                                            );

        if (predicate != null && !predicate.test(obj))
            throw JsParserException.reasonAt(ParserErrors.OBJ_CONDITION,
                                             reader.getPositionInStream()
                                            );
        return obj;

    }

    private void throwErrorIfStrictAndKeyMissing(final JsReader reader,
                                                 final String key
                                                ) {
        if (strict && !parsers.containsKey(key)) {
            throw JsParserException.reasonAt(ParserErrors.SPEC_NOT_FOUND.apply(key),
                                             reader.getPositionInStream()
                                            );
        }
    }


}
