package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfBigIntSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfBigInt arrayOfIntegralSpec;

  JsArrayOfBigIntSuchThat(final Function<JsArray, JsError> predicate,
                          final boolean nullable
                         ) {
    super(nullable);
    this.arrayOfIntegralSpec = new JsArrayOfBigInt(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfBigIntSuchThat(predicate,
                                       true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfIntegralSuchThat(predicate,
                                                        nullable);
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError result = arrayOfIntegralSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());
  }
}
