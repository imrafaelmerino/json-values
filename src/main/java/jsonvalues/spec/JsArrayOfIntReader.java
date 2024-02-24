package jsonvalues.spec;

import java.util.Objects;
import java.util.function.IntFunction;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfIntReader extends JsArrayReader {

  private final JsIntReader parser;

  JsArrayOfIntReader(final JsIntReader parser) {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(final DslJsReader reader,
                                  final IntFunction<JsError> fn,
                                  final int min,
                                  final int max
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   min,
                                   max);
  }

  JsArray arrayEachSuchThat(final DslJsReader reader,
                            final IntFunction<JsError> fn,
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
