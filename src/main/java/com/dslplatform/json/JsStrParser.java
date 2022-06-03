package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;
import java.util.Optional;
import java.util.function.Function;

final class JsStrParser extends AbstractParser {
    @Override
    JsStr value(final JsonReader<?> reader) {
        try {
            return JsStr.of(StringConverter.deserialize(reader));
        } catch (Exception e) {
            throw new JsParserException(e.getMessage());

        }
    }


    JsStr valueSuchThat(final JsonReader<?> reader,
                        final Function<String, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) {
        try {
            String value = StringConverter.deserialize(reader);
            Optional<Pair<JsValue,ERROR_CODE>> result = fn.apply(value);
            if (!result.isPresent()) return JsStr.of(value);
            throw reader.newParseError(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        } catch (Exception e) {
            throw new JsParserException(e.getMessage());

        }

    }

}
