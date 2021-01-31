package com.dslplatform.json;

import jsonvalues.JsStr;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsStrParser extends AbstractParser {
    @Override
    JsStr value(final JsonReader<?> reader){
        try {
            return JsStr.of(StringConverter.deserialize(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }


    JsStr valueSuchThat(final JsonReader<?> reader,
                        final Function<String, Optional<Error>> fn
                       ){
        try {
            final String          value  = StringConverter.deserialize(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsStr.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }

}
