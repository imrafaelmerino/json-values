package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

import java.util.Objects;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsArrayOfLongParser extends JsArrayParser {
    private final JsLongParser parser;

    JsArrayOfLongParser(final JsLongParser parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final LongFunction<Optional<JsError>> fn,
                                    final int min,
                                    final int max
    ) {
        return nullOrArrayEachSuchThat(reader,
                                       () -> parser.valueSuchThat(reader,
                                                                  fn),
                                       min,
                                       max);
    }


    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final LongFunction<Optional<JsError>> fn,
                              final int min,
                              final int max
    ) {
        return arrayEachSuchThat(reader,
                                 () -> parser.valueSuchThat(reader,
                                                            fn),
                                 min,
                                 max);
    }


}
