package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfObjParser extends JsArrayParser {

    private final JsObjParser parser;

    JsArrayOfObjParser(final JsObjParser parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final Function<JsObj, Optional<JsError>> fn,
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
                              final Function<JsObj, Optional<JsError>> fn,
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
