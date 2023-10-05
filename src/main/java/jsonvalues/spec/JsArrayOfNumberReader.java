package jsonvalues.spec;


import jsonvalues.JsArray;
import jsonvalues.JsNumber;
import jsonvalues.JsParserException;
import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfNumberReader extends JsArrayReader {

    private final JsNumberReader parser;

    JsArrayOfNumberReader(final JsNumberReader parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                    final Function<JsNumber, Optional<JsError>> fn,
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
                              final Function<JsNumber, Optional<JsError>> fn,
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
