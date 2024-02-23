package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;


final class JsArraySuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfValue isArray;

  JsArraySuchThat(final Function<JsArray, JsError> predicate,
                  final boolean nullable
                 ) {
    super(nullable);
    this.isArray = new JsArrayOfValue(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArraySuchThat(predicate,
                               true
    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfValueSuchThat(predicate,
                                                     nullable
                                                    );
  }

  @Override
  public JsError testValue(final JsValue value) {
    final JsError result = isArray.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());
  }
}
