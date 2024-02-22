package jsonvalues.spec;

import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleFunction;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

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
