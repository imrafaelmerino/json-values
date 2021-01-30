package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.NumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsInt;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntParser extends AbstractParser {
    @Override
    JsInt value(final JsonReader<?> reader){
        try {
            return JsInt.of(NumberConverter.deserializeInt(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsInt valueSuchThat(final JsonReader<?> reader,
                        final IntFunction<Optional<Error>> fn
                       ){
        try {
            final int             value  = NumberConverter.deserializeInt(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsInt.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
