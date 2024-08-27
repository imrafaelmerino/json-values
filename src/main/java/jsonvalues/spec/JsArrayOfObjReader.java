package jsonvalues.spec;


import java.util.Objects;
import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

final class JsArrayOfObjReader extends JsArrayReader {

  private final JsObjReader parser;

  JsArrayOfObjReader(final JsObjReader parser) {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  JsValue nullOrArrayEachSuchThat(final DslJsReader reader,
                                  final Function<JsObj, JsError> fn,
                                  ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {
    return nullOrArrayEachSuchThat(reader,
                                   () -> parser.valueSuchThat(reader,
                                                              fn),
                                   arrayConstraints);
  }


  JsArray arrayEachSuchThat(final DslJsReader reader,
                            final Function<JsObj, JsError> fn,
                            ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {
    return arrayEachSuchThat(reader,
                             () -> parser.valueSuchThat(reader,
                                                        fn),
                             arrayConstraints);
  }


}
