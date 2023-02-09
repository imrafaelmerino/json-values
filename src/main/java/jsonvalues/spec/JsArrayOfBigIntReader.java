package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfBigIntReader extends JsArrayReader {

    private final JsBigIntReader parser;

    JsArrayOfBigIntReader(final JsBigIntReader parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                    final Function<BigInteger, Optional<JsError>> fn,
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
                              final Function<BigInteger, Optional<JsError>> fn,
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
