package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

import jsonvalues.JsValue;

final class JsLongSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final LongSchemaConstraints constraints;

  JsLongSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  JsLongSpec(final boolean nullable,
             final LongSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsLongSpec(true,
                          constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofLong(nullable);
  }

  @Override
  public JsError testValue(final JsValue value) {
    var error =
        Functions.testValue(JsValue::isLong,
                            LONG_EXPECTED,
                            nullable,
                            value
                           );
    if (error != null) {
      return error;
    }

    if (constraints != null) {
      return Functions.testLongConstraints(constraints,
                                           value.toJsLong());
    }

    return null;
  }


}
