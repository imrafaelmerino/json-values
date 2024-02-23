package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

import jsonvalues.JsValue;

final class JsDoubleSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final DoubleSchemaConstraints constraints;

  JsDoubleSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  JsDoubleSpec(final boolean nullable,
               DoubleSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsDoubleSpec(true,
                            constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofDouble(nullable);
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testValue(JsValue::isDouble,
                               DOUBLE_EXPECTED,
                               nullable,
                               value
                              );
  }


}
