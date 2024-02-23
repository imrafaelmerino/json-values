package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final IntegerSchemaConstraints constraints;

  JsArrayOfInt(final boolean nullable) {
    this(nullable,
         null);
  }

  JsArrayOfInt(final boolean nullable,
               IntegerSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  JsArrayOfInt(final boolean nullable,
               int min,
               int max
              ) {
    this(nullable,
         min,
         max,
         null);
  }

  JsArrayOfInt(final boolean nullable,
               int min,
               int max,
               IntegerSchemaConstraints constraints
              ) {
    super(nullable,
          min,
          max);
    this.constraints = constraints;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfInt(true,
                            min,
                            max,
                            constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfInt(nullable,
                                           min,
                                           max);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v -> v.isInt() ?
                                                 null :
                                                 new JsError(v,
                                                             INT_EXPECTED),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );

  }
}
