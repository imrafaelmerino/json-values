package jsonvalues.spec;

import jsonvalues.JsObj;

import java.util.Map;
import java.util.function.Predicate;

class JsObjSpecReader extends AbstractJsObjReader {
    private static final JsValueReader valueParser = JsReaders.READERS.valueReader;
    private static final JsParser defaultParser = valueParser::nullOrValue;
    final boolean strict;
    private final Map<String, JsParser> parsers;
    private final MetaData metadata;

    protected final Predicate<JsObj> predicate;

    JsObjSpecReader(boolean strict,
                    Map<String, JsParser> parsers,
                    Predicate<JsObj> predicate,
                    MetaData metadata
                   ) {
        this.strict = strict;
        this.parsers = parsers;
        this.predicate = predicate;
        this.metadata = metadata;
    }


    @Override
    JsObj value(final JsReader reader) throws JsParserException {
        if (isEmptyObj(reader)) return addDefaultFieldsIfSpecified(EMPTY_OBJ);
        var key = reader.readKey();
        var parser = parsers.get(key);
        if (parser == null) {
            var aliasField = metadata != null ? metadata.getAliasField(key) : null;
            if (aliasField != null) {
                parser = parsers.get(aliasField);
                key = aliasField;
            }
        }
        throwErrorIfStrictAndKeyMissing(reader,
                                        parser,
                                        key
                                       );
        parser = parser != null ? parser : defaultParser;
        var obj = EMPTY_OBJ.set(key,
                                parser
                                        .parse(reader)
                               );
        byte nextToken;
        while ((nextToken = reader.readNextToken()) == ',') {
            reader.readNextToken();
            key = reader.readKey();
            parser = parsers.get(key);
            if (parser == null) {
                var aliasField = metadata != null ? metadata.getAliasField(key) : null;
                if (aliasField != null) {
                    parser = parsers.get(aliasField);
                    key = aliasField;
                }
            }
            throwErrorIfStrictAndKeyMissing(reader,
                                            parser,
                                            key
                                           );
            parser = parser != null ? parser : defaultParser;
            obj = obj.set(key,
                          parser.parse(reader)
                         );

        }
        if (nextToken != '}')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_MAP_END.formatted(((char) nextToken)),
                                             reader.getPositionInStream()
                                            );
        obj = addDefaultFieldsIfSpecified(obj);

        if (predicate != null && !predicate.test(obj))
            throw JsParserException.reasonAt(ParserErrors.OBJ_CONDITION,
                                             reader.getPositionInStream()
                                            );

        return obj;

    }

    private JsObj addDefaultFieldsIfSpecified(JsObj obj) {
        if (metadata != null && metadata.fieldsDefault() != null) {
            for (String defaultKey : metadata.fieldsDefault().keySet()) {
                if (obj.get(defaultKey).isNothing())
                    obj = obj.set(defaultKey,
                                  metadata.fieldsDefault()
                                          .get(defaultKey));
            }
        }
        return obj;
    }

    private void throwErrorIfStrictAndKeyMissing(final JsReader reader,
                                                 final JsParser keyParser,
                                                 final String key
                                                ) {
        if (strict && keyParser == null) {
            throw JsParserException.reasonAt(ParserErrors.SPEC_NOT_FOUND.apply(key),
                                             reader.getPositionInStream()
                                            );
        }
    }


}
