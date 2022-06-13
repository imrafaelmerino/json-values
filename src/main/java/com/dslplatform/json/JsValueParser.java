package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsBool;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.util.Optional;
import java.util.function.Function;

final class JsValueParser extends AbstractParser {
    private JsObjParser objDeserializer;
    private JsArrayOfValueParser arrayDeserializer;
    private JsNumberParser numberDeserializer;

    void setNumberDeserializer(final JsNumberParser numberDeserializer) {
        this.numberDeserializer = numberDeserializer;
    }

    void setObjDeserializer(final JsObjParser objDeserializer) {
        this.objDeserializer = objDeserializer;
    }

    void setArrayDeserializer(final JsArrayOfValueParser arrayDeserializer) {
        this.arrayDeserializer = arrayDeserializer;
    }

    JsValue valueSuchThat(final JsonReader<?> reader,
                          final Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> fn

    ) {
        try {
            JsValue value = value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
            if (!result.isPresent()) return value;
            throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                        reader.getCurrentIndex());
        } catch (JsParserException e) {
            throw e;

        } catch (Exception e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());

        }

    }

    @Override
    JsValue value(final JsonReader<?> reader) {
        try {
            switch (reader.last()) {
                case 't':
                    if (!reader.wasTrue()) {
                        throw new JsParserException(ParserErrors.EXPECTING_TRUE,
                                                    reader.getCurrentIndex()
                        );
                    }
                    return JsBool.TRUE;
                case 'f':
                    if (!reader.wasFalse()) {
                        throw new JsParserException(ParserErrors.EXPECTING_FALSE,
                                                    reader.getCurrentIndex());
                    }
                    return JsBool.FALSE;
                case '"':
                    return JsStr.of(reader.readString());
                case '{':
                    return objDeserializer.value(reader);
                case '[':
                    return arrayDeserializer.array(reader);
                default:
                    return numberDeserializer.value(reader);
            }
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } catch (JsParserException e) {
            throw e;
        } catch (Exception e) {
            throw new JsParserException(e,
                                        reader.getCurrentIndex());
        }
    }

}
