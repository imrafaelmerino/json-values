package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfLongSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfLong arrayOfLongSpec;

  JsArrayOfLongSuchThat(final Function<JsArray, JsError> predicate,
                        final boolean nullable
                       ) {
    super(nullable);
    this.arrayOfLongSpec = new JsArrayOfLong(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfLongSuchThat(predicate,
                                     true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfLongSuchThat(predicate,
                                                    nullable
                                                   );
  }


  @Override
  public JsError testValue(final JsValue value) {
    JsError result = arrayOfLongSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());
  }
}
