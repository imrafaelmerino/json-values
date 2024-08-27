package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final IntegerSchemaConstraints constraints;

  JsArrayOfInt(final boolean nullable) {
    this(nullable,
         null,
         null);
  }

  JsArrayOfInt(final boolean nullable,
               IntegerSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  JsArrayOfInt(final boolean nullable,
               ArraySchemaConstraints arrayConstraints
              ) {
    this(nullable,
         arrayConstraints,
         null);
  }

  JsArrayOfInt(boolean nullable,
               ArraySchemaConstraints arrayConstraints,
               IntegerSchemaConstraints constraints
              ) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfInt(true,
                            arrayConstraints,
                            constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfInt(nullable,
                                           arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> v.isInt() ?
                                           null :
                                           new JsError(v,
                                                       INT_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );

  }
}
