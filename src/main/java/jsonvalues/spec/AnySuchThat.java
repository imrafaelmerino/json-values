package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.REQUIRED;

import java.util.function.Function;
import jsonvalues.JsValue;

final class AnySuchThat implements JsOneErrorSpec {

  private final Function<JsValue, JsError> predicate;

  AnySuchThat(final Function<JsValue, JsError> predicate) {
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    throw new RuntimeException("not allowed for AnySuchThat. Just use the predicate...");
  }

  @Override
  public boolean isNullable() {
    return false;
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofValueSuchThat(predicate);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return value.isNothing() ?
           new JsError(value,
                       REQUIRED) :
           predicate.apply(value);

  }
}
