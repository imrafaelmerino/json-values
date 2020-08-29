package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsInt;
import jsonvalues.spec.Error;

import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntParser extends AbstractParser {
    @Override
    JsInt value(final JsonReader<?> reader){
        try {
            return JsInt.of(MyNumberConverter.parseInt(reader));
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsInt valueSuchThat(final JsonReader<?> reader,
                        final IntFunction<Optional<Error>> fn
                       ){
        try {
            final int             value  = MyNumberConverter.parseInt(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsInt.of(value);
            throw reader.newParseError(result.toString());
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
