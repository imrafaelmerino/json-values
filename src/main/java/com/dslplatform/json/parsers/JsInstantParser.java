package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsInstant;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

final class JsInstantParser extends AbstractParser {
    @Override
    JsInstant value(final JsonReader<?> reader) throws JsParserException {
        try {
            return JsInstant.of(Instant.from(ISO_INSTANT.parse(reader.readString())));
        } catch (IOException | DateTimeParseException e) {
            throw new JsParserException(e.getMessage());
        }

    }

    JsInstant valueSuchThat(final JsonReader<?> reader,
                            final Function<Instant,Optional<Error>> fn
                       ) throws JsParserException {
        try {
            final Instant            value  = Instant.from(ISO_INSTANT.parse(reader.readString()));
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsInstant.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException | DateTimeParseException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
