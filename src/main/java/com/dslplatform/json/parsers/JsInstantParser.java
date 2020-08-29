package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsInstant;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;


import static java.time.format.DateTimeFormatter.ISO_INSTANT;

final class JsInstantParser extends AbstractParser {
    @Override
    JsInstant value(final JsonReader<?> reader){
        try {
            return JsInstant.of(Instant.from(ISO_INSTANT.parse(reader.readString())));
        } catch (IOException | DateTimeParseException e) {
            throw new JsParserException(e.getMessage());
        }

    }

}
