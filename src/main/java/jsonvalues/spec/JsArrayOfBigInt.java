package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfBigInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  BigIntSchemaConstraints constraints;

  JsArrayOfBigInt(boolean nullable) {
    super(nullable);
  }

  JsArrayOfBigInt(boolean nullable,
                  ArraySchemaConstraints arrayConstraints,
                  BigIntSchemaConstraints constraints
                 ) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfBigInt(true,
                               arrayConstraints,
                               constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfIntegral(nullable,
                                                arrayConstraints,
                                                constraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> {
                                        if (!v.isIntegral()) {
                                          return new JsError(v,
                                                             INTEGRAL_EXPECTED);
                                        }
                                        if (constraints != null) {
                                          var errorCode = Fun.testBigIntConstraints(constraints,
                                                                                     v.toJsBigInt());
                                          if (errorCode != null) {
                                            return new JsError(v,
                                                               errorCode);
                                          }
                                        }
                                        return null;
                                      },
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
