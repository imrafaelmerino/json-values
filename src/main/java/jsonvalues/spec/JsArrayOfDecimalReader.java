package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfDecimalReader extends JsArrayReader {

  private final JsDecimalReader parser;

  JsArrayOfDecimalReader(final JsDecimalReader parser) {
    super(requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(DslJsReader reader,
                                  Function<BigDecimal, JsError> fn,
                                  ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   arrayConstraints);
  }

  JsArray arrayEachSuchThat(DslJsReader reader,
                            Function<BigDecimal, JsError> fn,
                            ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {
    return arrayEachSuchThat(reader,
                             () -> parser.valueSuchThat(reader,
                                                        fn),
                             arrayConstraints);

  }


}
