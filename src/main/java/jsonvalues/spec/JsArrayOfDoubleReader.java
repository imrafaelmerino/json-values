package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleFunction;

final class JsArrayOfDoubleReader extends JsArrayReader {
    private final JsDoubleReader parser;

    JsArrayOfDoubleReader(final JsDoubleReader parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                    final DoubleFunction<Optional<JsError>> fn,
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
                              final DoubleFunction<Optional<JsError>> fn,
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
