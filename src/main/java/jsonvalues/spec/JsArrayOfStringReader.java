package jsonvalues.spec;

import java.util.Objects;
import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfStringReader extends JsArrayReader {

  private final JsStrReader parser;

  JsArrayOfStringReader(final JsStrReader reader) {
    super(Objects.requireNonNull(reader));
    this.parser = reader;
  }

  JsValue nullOrArrayEachSuchThat(DslJsReader reader,
                                  Function<String, JsError> fn,
                                  ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   arrayConstraints);
  }


  JsArray arrayEachSuchThat(DslJsReader reader,
                            Function<String, JsError> fn,
                            ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {
    return arrayEachSuchThat(reader,
                             () -> parser.valueSuchThat(reader,
                                                        fn),
                             arrayConstraints);
  }


}
