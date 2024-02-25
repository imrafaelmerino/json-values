package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

import jsonvalues.JsValue;

final class JsInstantSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  JsInstantSpec(final boolean nullable) {
    super(nullable);
  }

  @Override
  public JsSpec nullable() {
    return new JsInstantSpec(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofInstant(nullable);
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testValue(JsValue::isInstant,
                         INSTANT_EXPECTED,
                         nullable,
                         value
                        );

  }


}
