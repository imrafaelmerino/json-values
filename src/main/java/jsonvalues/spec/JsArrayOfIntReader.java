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

  JsValue nullOrArrayEachSuchThat(DslJsReader reader,
                                  IntFunction<JsError> fn,
                                  ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   arrayConstraints);
  }

  JsArray arrayEachSuchThat(DslJsReader reader,
                            IntFunction<JsError> fn,
                            ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {
    return arrayEachSuchThat(reader,
                             () -> parser.valueSuchThat(reader,
                                                        fn),
                             arrayConstraints);
  }

}
