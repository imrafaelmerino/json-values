package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import jsonvalues.JsLong;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsLongParser extends AbstractParser {

    @Override
    JsLong value(final JsonReader<?> reader) throws JsParserException {
        try {
            return JsLong.of(MyNumberConverter.parseLong(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsLong valueSuchThat(final JsonReader<?> reader,
                         final LongFunction<Optional<Error>> fn
                        ) throws JsParserException {
        try {
            final long            value  = MyNumberConverter.parseLong(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsLong.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
