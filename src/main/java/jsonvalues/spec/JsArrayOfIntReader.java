package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsParserException;
import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsArrayOfIntReader extends JsArrayReader {
    private final JsIntReader parser;

    JsArrayOfIntReader(final JsIntReader parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                    final IntFunction<Optional<JsError>> fn,
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
                              final IntFunction<Optional<JsError>> fn,
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
