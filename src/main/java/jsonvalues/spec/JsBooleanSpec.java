package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

import jsonvalues.JsValue;

final class JsBooleanSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  JsBooleanSpec(final boolean nullable) {
    super(nullable);
  }


  @Override
  public JsSpec nullable() {
    return new JsBooleanSpec(true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofBool(nullable);
  }

  @Override
  public JsError testValue(final JsValue value) {

    return Fun.testValue(JsValue::isBool,
                         BOOLEAN_EXPECTED,
                         nullable,
                         value
                        );

  }


}
