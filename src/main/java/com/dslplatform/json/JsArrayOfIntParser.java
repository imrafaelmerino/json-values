package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
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
                                    final IntFunction<Optional<Pair<JsValue, ERROR_CODE>>> fn,
                                    final int min,
                                    final int max
    ) throws IOException {
        return nullOrArrayEachSuchThat(reader,
                                       () -> parser.valueSuchThat(reader,
                                                                  fn),
                                       min,
                                       max);
    }

    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final IntFunction<Optional<Pair<JsValue,ERROR_CODE>>> fn,
                              final int min,
                              final int max
    ) throws IOException {
        return arrayEachSuchThat(reader,
                                 () -> parser.valueSuchThat(reader,
                                                            fn),
                                 min,
                                 max);
    }

}
