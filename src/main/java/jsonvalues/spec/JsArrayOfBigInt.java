package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfBigInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  JsArrayOfBigInt(final boolean nullable) {
    super(nullable);
  }

  JsArrayOfBigInt(final boolean nullable,
                  ArraySchemaConstraints arrayConstraints
                 ) {
    super(nullable,
          arrayConstraints);
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfBigInt(true,
                               arrayConstraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE
        .ofArrayOfIntegral(nullable,
                           arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v ->
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
