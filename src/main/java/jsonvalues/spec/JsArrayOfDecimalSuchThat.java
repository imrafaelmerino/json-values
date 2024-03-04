package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfDecimalSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfDecimal arrayOfDecimalSpec;

  JsArrayOfDecimalSuchThat(final Function<JsArray, JsError> predicate,
                           final boolean nullable
                          ) {
    super(nullable);
    this.arrayOfDecimalSpec = new JsArrayOfDecimal(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfDecimalSuchThat(predicate,
                                        true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDecimalSuchThat(predicate,
                                                       nullable);
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError result = arrayOfDecimalSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());
  }
}
