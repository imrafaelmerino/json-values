package jsonvalues.spec;

import java.util.Objects;
import java.util.function.LongFunction;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfLongReader extends JsArrayReader {

  private final JsLongReader parser;

  JsArrayOfLongReader(final JsLongReader parser) {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(final DslJsReader reader,
                                  final LongFunction<JsError> fn,
                                  final ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   arrayConstraints);
  }


  JsArray arrayEachSuchThat(final DslJsReader reader,
                            final LongFunction<JsError> fn,
                            final ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {
    return arrayEachSuchThat(reader,
                             () -> parser.valueSuchThat(reader,
                                                        fn),
                             arrayConstraints);
  }


}
