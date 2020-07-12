package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsBinary;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsBinaryParser extends AbstractParser {
    @Override
    JsBinary value(final JsonReader<?> reader) throws JsParserException {
        try {
            return JsBinary.of(reader.readBase64());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsBinary valueSuchThat(final JsonReader<?> reader,
                        final Function<byte[],Optional<Error>> fn
                       ) throws JsParserException {
        try {
            final byte[]          value  = reader.readBase64();
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsBinary.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
