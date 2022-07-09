package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsInt;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntParser extends AbstractParser {
    @Override
    JsInt value(final JsonReader<?> reader) throws ParsingException {
        return JsInt.of(MyNumberConverter.deserializeInt(reader));
    }

    JsInt valueSuchThat(final JsonReader<?> reader,
                        final IntFunction<Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) throws ParsingException {
            int value = MyNumberConverter.deserializeInt(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
            if (!result.isPresent()) return JsInt.of(value);
            throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                        reader.getCurrentIndex());
    }


}
