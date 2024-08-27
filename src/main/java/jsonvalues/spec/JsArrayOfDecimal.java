package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfDecimal extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final DecimalSchemaConstraints constraints;

  JsArrayOfDecimal(final boolean nullable) {
    this(nullable,
         null);
  }

  JsArrayOfDecimal(final boolean nullable,
                   DecimalSchemaConstraints constraints,
                   ArraySchemaConstraints arrayConstraints) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;

  }

  JsArrayOfDecimal(final boolean nullable,
                   ArraySchemaConstraints arrayConstraints
                  ) {
    this(nullable,
         arrayConstraints,
         null);
  }

  JsArrayOfDecimal(final boolean nullable,
                   ArraySchemaConstraints arrayConstraints,
                   DecimalSchemaConstraints constraints
                  ) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfDecimal(true,
                                arrayConstraints,
                                constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDecimal(nullable,
                                               arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> v.isNumber() ?
                                           null :
                                           new JsError(v,
                                                       DECIMAL_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
