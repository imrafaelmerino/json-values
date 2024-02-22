package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfDecimalReader extends JsArrayReader {

  private final JsDecimalReader parser;

  JsArrayOfDecimalReader(final JsDecimalReader parser) {
    super(requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                  final Function<BigDecimal, Optional<JsError>> fn,
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
                            final Function<BigDecimal, Optional<JsError>> fn,
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
