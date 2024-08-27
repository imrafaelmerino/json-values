package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

import jsonvalues.JsValue;

final class IsJsObj extends AbstractNullable implements JsOneErrorSpec {

  IsJsObj(final boolean nullable) {
    super(nullable);
  }

  @Override
  public JsSpec nullable() {
    return new IsJsObj(true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofObj(nullable);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testValue(JsValue::isObj,
                         OBJ_EXPECTED,
                         nullable,
                         value);

  }
}
