package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfIntSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfInt arrayOfIntSpec;

  JsArrayOfIntSuchThat(final Function<JsArray, JsError> predicate,
                       final boolean nullable
                      ) {
    super(nullable);
    this.arrayOfIntSpec = new JsArrayOfInt(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfIntSuchThat(predicate,
                                    true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfIntSuchThat(predicate,
                                                   nullable
                                                  );
  }


  @Override
  public JsError testValue(final JsValue value) {
    JsError result = arrayOfIntSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());
  }
}
