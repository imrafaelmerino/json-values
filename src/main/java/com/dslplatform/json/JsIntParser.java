package com.dslplatform.json;

import jsonvalues.JsInt;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntParser extends AbstractParser {
    @Override
    JsInt value(final JsonReader<?> reader){
        try {
            return JsInt.of(MyNumberConverter.deserializeInt(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsInt valueSuchThat(final JsonReader<?> reader,
                        final IntFunction<Optional<Error>> fn
                       ){
        try {
            final int             value  = MyNumberConverter.deserializeInt(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsInt.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
