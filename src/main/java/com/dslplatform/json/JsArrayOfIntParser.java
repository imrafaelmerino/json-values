package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsArrayOfIntParser extends JsArrayParser {
    private final JsIntParser parser;

    JsArrayOfIntParser(final JsIntParser parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final IntFunction<Optional<JsError>> fn,
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
                              final IntFunction<Optional<JsError>> fn,
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
