package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfDouble extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final DoubleSchemaConstraints constraints;

  JsArrayOfDouble(final boolean nullable) {
    this(nullable,
         null);
  }

  JsArrayOfDouble(final boolean nullable,
                  DoubleSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  JsArrayOfDouble(final boolean nullable,
                  int min,
                  int max
                 ) {
    this(nullable,
         min,
         max,
         null);
  }

  JsArrayOfDouble(final boolean nullable,
                  int min,
                  int max,
                  DoubleSchemaConstraints constraints
                 ) {
    super(nullable,
          min,
          max);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfDouble(true,
                               min,
                               max,
                               constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDouble(nullable,
                                              min,
                                              max);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v -> v.isDouble() ?
                                                 null :
                                                 new JsError(v,
                                                             DOUBLE_EXPECTED),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }
}
