package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfStringReader extends JsArrayReader {
    private final JsStrReader parser;

    JsArrayOfStringReader(final JsStrReader reader) {
        super(Objects.requireNonNull(reader));
        this.parser = reader;
    }

    JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                    final Function<String, Optional<JsError>> fn,
                                    final int min,
                                    final int max
                                   ) throws JsParserException {
        return nullOrArrayEachSuchThat(reader,
                                       () -> parser.valueSuchThat(reader,
                                                                  fn),
                                       min,
                                       max);
    }


    JsArray arrayEachSuchThat(final JsReader reader,
                              final Function<String, Optional<JsError>> fn,
                              final int min,
                              final int max
                             ) throws JsParserException {
        return arrayEachSuchThat(reader,
                                 () -> parser.valueSuchThat(reader,
                                                            fn),
                                 min,
                                 max);
    }


}
