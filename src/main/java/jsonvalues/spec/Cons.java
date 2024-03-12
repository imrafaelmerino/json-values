package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.CONSTANT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.REQUIRED;

import java.util.Objects;
import jsonvalues.JsValue;

final class Cons implements JsOneErrorSpec, AvroSpec {

  final String name;
  final JsValue value;

  Cons(final JsValue value,
       final String name) {
    if (!value.isPrimitive()) {
      throw new IllegalArgumentException("The constant  must be a primitive value");
    }
    this.value = Objects.requireNonNull(value);
    this.name = Objects.requireNonNull(name);
  }

  Cons(final JsValue value) {
    this(value,
         "");
  }

  @Override
  public JsError testValue(final JsValue value) {
    if (value.isNothing()) {
      new JsError(value,
                  REQUIRED);
    }

    if (!value.equals(this.value)) {
      return new JsError(value,
                         CONSTANT_CONDITION);
    }
    return null;
  }

  @Override
  public JsSpec nullable() {
    throw new RuntimeException("not allowed for constants");
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofConstant(value);
  }

  @Override
  public boolean isNullable() {
    return false;
  }
}
