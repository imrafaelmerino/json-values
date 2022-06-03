package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfBigIntParser extends JsArrayParser {

    private final JsBigIntParser parser;

    JsArrayOfBigIntParser(final JsBigIntParser parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> fn,
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
                              final Function<BigInteger, Optional<Pair<JsValue,ERROR_CODE>>> fn,
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
