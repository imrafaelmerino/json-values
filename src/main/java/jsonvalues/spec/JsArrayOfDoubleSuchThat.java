package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;


final class JsArrayOfDoubleSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfDouble arrayOfDoubleSpec;

  JsArrayOfDoubleSuchThat(final Function<JsArray, JsError> predicate,
                          final boolean nullable
                         ) {
    super(nullable);
    this.arrayOfDoubleSpec = new JsArrayOfDouble(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfDoubleSuchThat(predicate,
                                       true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDoubleSuchThat(predicate,
                                                      nullable);
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError result = arrayOfDoubleSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());

  }
}
