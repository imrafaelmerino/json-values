package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

import jsonvalues.JsValue;

final class JsIntSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final IntegerSchemaConstraints constraints;

  JsIntSpec(final boolean nullable,
            final IntegerSchemaConstraints constraints
           ) {
    super(nullable);
    this.constraints = constraints;
  }

  JsIntSpec(final boolean nullable) {
    this(nullable,
         null
        );
  }


  @Override
  public JsSpec nullable() {
    return new JsIntSpec(true,
                         constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofInt(nullable);
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testValue(JsValue::isInt,
                               INT_EXPECTED,
                               nullable,
                               value
                              );

  }


}
