package jsonvalues.spec;

import java.util.Objects;
import java.util.function.DoubleFunction;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfDoubleReader extends JsArrayReader {

  private final JsDoubleReader parser;

  JsArrayOfDoubleReader(final JsDoubleReader parser) {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(final DslJsReader reader,
                                  final DoubleFunction<JsError> fn,
                                  ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   arrayConstraints);
  }


  JsArray arrayEachSuchThat(final DslJsReader reader,
                            final DoubleFunction<JsError> fn,
                            ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {
    return arrayEachSuchThat(reader,
                             () -> parser.valueSuchThat(reader,
                                                        fn),
                             arrayConstraints);
  }


}
