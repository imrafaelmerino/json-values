package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfObjSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfObj arrayOfObjSpec;

  JsArrayOfObjSuchThat(final Function<JsArray, JsError> predicate,
                       final boolean nullable
                      ) {
    super(nullable);
    this.arrayOfObjSpec = new JsArrayOfObj(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfObjSuchThat(predicate,
                                    true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfObjSuchThat(predicate,
                                                   nullable);
  }


  @Override
  public JsError testValue(final JsValue value) {
    JsError result = arrayOfObjSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());
  }
}
