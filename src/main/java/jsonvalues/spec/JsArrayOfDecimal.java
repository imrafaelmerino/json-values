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
                   DecimalSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  JsArrayOfDecimal(final boolean nullable,
                   int min,
                   int max
                  ) {
    this(nullable,
         min,
         max,
         null);
  }

  JsArrayOfDecimal(final boolean nullable,
                   int min,
                   int max,
                   DecimalSchemaConstraints constraints
                  ) {
    super(nullable,
          min,
          max);
    this.constraints = constraints;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfDecimal(true,
                                min,
                                max,
                                constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDecimal(nullable,
                                               min,
                                               max);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v -> v.isNumber() ?
                                                 null :
                                                 new JsError(v,
                                                             DECIMAL_EXPECTED),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }
}
