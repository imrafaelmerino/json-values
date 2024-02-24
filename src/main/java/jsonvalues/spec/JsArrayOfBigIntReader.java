package jsonvalues.spec;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfBigIntReader extends JsArrayReader {

  private final JsBigIntReader parser;

  JsArrayOfBigIntReader(final JsBigIntReader parser) {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(final DslJsReader reader,
                                  final Function<BigInteger, JsError> fn,
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
                            final Function<BigInteger, JsError> fn,
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
