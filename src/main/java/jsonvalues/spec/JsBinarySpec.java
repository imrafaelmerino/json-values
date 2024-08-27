package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

import jsonvalues.JsValue;

final class JsBinarySpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  JsBinarySpec(final boolean nullable) {
    super(nullable);
  }


  @Override
  public JsSpec nullable() {
    return new JsBinarySpec(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofBinary(nullable);
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testValue(JsValue::isBinary,
                         BINARY_EXPECTED,
                         nullable,
                         value
                        );

  }


}
