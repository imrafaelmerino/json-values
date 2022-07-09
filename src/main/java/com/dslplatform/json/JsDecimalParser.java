package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsBigDec;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;


final class JsDecimalParser extends AbstractParser {

    @Override
    JsBigDec value(final JsonReader<?> reader) throws IOException {
            return JsBigDec.of(MyNumberConverter.deserializeDecimal(reader));
    }

    JsBigDec valueSuchThat(final JsonReader<?> reader,
                           final Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) throws IOException {
            final BigDecimal value = MyNumberConverter.deserializeDecimal(reader);
            final Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
            if (!result.isPresent()) return JsBigDec.of(value);
            throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                        reader.getCurrentIndex());
    }


}
