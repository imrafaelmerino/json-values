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
    return Fun.testArrayOfTestedValue(v ->
                                          v.isIntegral() ?
                                          null :
                                          new JsError(v,
                                                      INTEGRAL_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
