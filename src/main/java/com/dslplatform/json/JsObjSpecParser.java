package com.dslplatform.json;

import jsonvalues.JsObj;

import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;

class JsObjSpecParser extends AbstractJsObjParser {
    private static final JsValueParser valueParser = JsParsers.PARSERS.valueParser;
    private static final JsSpecParser defaultParser = valueParser::nullOrValue;
    protected final boolean strict;
    private final Map<String, JsSpecParser> parsers;

    protected Predicate<JsObj> predicate;

    JsObjSpecParser(boolean strict,
                    Map<String, JsSpecParser> parsers,
                    Predicate<JsObj> predicate
    ) {
        this.strict = strict;
        this.parsers = parsers;
        this.predicate = predicate;
    }

    @Override
    JsObj value(final JsonReader<?> reader) {
        try {
            if (isEmptyObj(reader)) return EMPTY_OBJ;
            String key = reader.readKey();
            throwErrorIfStrictAndKeyMissing(reader,
                                            key
            );
            JsSpecParser parser = parsers.getOrDefault(key,
                                                       defaultParser);
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
                                                              defaultParser);
                obj = obj.set(key,
                              keyparser.parse(reader)
                );

            }
            if (nextToken != '}')
                throw new JsParserException(ParserErrors.EXPECTING_FOR_MAP_END,
                                            reader.getCurrentIndex());

            if (predicate != null && !predicate.test(obj))
                throw new JsParserException(ParserErrors.OBJ_CONDITION,
                                            reader.getCurrentIndex());
            return obj;
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

    private void throwErrorIfStrictAndKeyMissing(final JsonReader<?> reader,
                                                 final String key) throws JsParserException {
        if (strict && !parsers.containsKey(key)) {
            throw new JsParserException(ParserErrors.SPEC_NOT_FOUND.apply(key),
                                        reader.getCurrentIndex());
        }
    }


}
