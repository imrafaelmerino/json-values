package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.NumberConverter;
import jsonvalues.JsLong;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsLongParser extends AbstractParser {

    @Override
    JsLong value(final JsonReader<?> reader) {
        try {
            return JsLong.of(NumberConverter.deserializeLong(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsLong valueSuchThat(final JsonReader<?> reader,
                         final LongFunction<Optional<Error>> fn
                        ) {
        try {
            final long            value  = NumberConverter.deserializeLong(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsLong.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
