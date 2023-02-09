package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsArrayOfLongReader extends JsArrayReader {
    private final JsLongReader parser;

    JsArrayOfLongReader(final JsLongReader parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                    final LongFunction<Optional<JsError>> fn,
                                    final int min,
                                    final int max
    ) throws IOException {
        return nullOrArrayEachSuchThat(reader,
                                       () -> parser.valueSuchThat(reader,
                                                                  fn),
                                       min,
                                       max);
    }


    JsArray arrayEachSuchThat(final JsReader reader,
                              final LongFunction<Optional<JsError>> fn,
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
