package jsonvalues.spec;

import java.util.Objects;
import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfValueReader extends JsArrayReader {

  private final JsValueReader parser;

  JsArrayOfValueReader(final JsValueReader parser) {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(DslJsReader reader,
                                  Function<JsValue, JsError> fn,
                                  ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   arrayConstraints);
  }


  JsArray arrayEachSuchThat(DslJsReader reader,
                            Function<JsValue, JsError> fn,
                            ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {
    return arrayEachSuchThat(reader,
                             () -> parser.valueSuchThat(reader,
                                                        fn),
                             arrayConstraints);
  }


}
