package com.dslplatform.json;

import jsonvalues.JsBigDec;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;


final class JsDecimalParser extends AbstractParser {

    @Override
    JsBigDec value(final JsonReader<?> reader) {
        try {
            return JsBigDec.of(MyNumberConverter.deserializeDecimal(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsBigDec valueSuchThat(final JsonReader<?> reader,
                           final Function<BigDecimal, Optional<JsError>> fn
    ) {
        try {
            final BigDecimal value = MyNumberConverter.deserializeDecimal(reader);
            final Optional<JsError> result = fn.apply(value);
            if (!result.isPresent()) return JsBigDec.of(value);
            throw reader.newParseError(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }


}
