package jsonvalues.spec;

import jsonvalues.JsValue;

final class AnySpec implements JsOneErrorSpec {

  @Override
  public JsSpec nullable() {
    throw new RuntimeException("not allowed for AnySpec. Null is already included...");
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofValue();
  }


  @Override
  public boolean isNullable() {
    return true;
  }

  @Override
  public JsError testValue(final JsValue value) {
    return value.isNothing() ?
           new JsError(value,
                       ERROR_CODE.REQUIRED) :
           null;
  }
}
