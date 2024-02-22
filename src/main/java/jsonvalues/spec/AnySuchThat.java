package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.REQUIRED;

import java.util.Optional;
import java.util.function.Function;
import jsonvalues.JsValue;

final class AnySuchThat implements JsOneErrorSpec {

  private final Function<JsValue, Optional<JsError>> predicate;

  AnySuchThat(final Function<JsValue, Optional<JsError>> predicate) {
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return this;
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
  public Optional<JsError> testValue(final JsValue value) {
    return value.isNothing() ?
           Optional.of(new JsError(value,
                                   REQUIRED)) :
           predicate.apply(value);

  }
}
