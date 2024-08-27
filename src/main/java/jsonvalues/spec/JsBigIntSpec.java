package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

import jsonvalues.JsValue;

final class JsBigIntSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final BigIntSchemaConstraints constraints;

  JsBigIntSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  JsBigIntSpec(final boolean nullable,
               BigIntSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsBigIntSpec(true,
                            constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofIntegral(nullable,
                                         constraints
                                        );
  }


  @Override
  public JsError testValue(final JsValue value) {
    var error =
        Fun.testValue(JsValue::isIntegral,
                      INTEGRAL_EXPECTED,
                      nullable,
                      value
                     );
    if (error != null) {
      return error;
    }

    if (constraints != null) {
      var errorCode = Fun.testBigIntConstraints(constraints,
                                                value.toJsBigInt());
      if (errorCode != null) {
        return new JsError(value,
                           errorCode);
      }
    }

    return null;

  }
}
