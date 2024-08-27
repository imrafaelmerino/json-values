package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;


final class JsArrayOfBoolSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfBool arrayOfBoolSpec;

  JsArrayOfBoolSuchThat(final Function<JsArray, JsError> predicate,
                        final boolean nullable
                       ) {
    super(nullable);
    this.arrayOfBoolSpec = new JsArrayOfBool(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfBoolSuchThat(predicate,
                                     true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfBoolSuchThat(predicate,
                                                    nullable);
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError result = arrayOfBoolSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());

  }
}
