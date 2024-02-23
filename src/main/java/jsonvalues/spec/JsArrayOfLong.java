package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfLong extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final LongSchemaConstraints constraints;

  JsArrayOfLong(final boolean nullable) {
    this(nullable,
         null);
  }

  JsArrayOfLong(final boolean nullable,
                LongSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  JsArrayOfLong(final boolean nullable,
                int min,
                int max
               ) {
    this(nullable,
         min,
         max,
         null);
  }

  JsArrayOfLong(final boolean nullable,
                int min,
                int max,
                LongSchemaConstraints constraints
               ) {
    super(nullable,
          min,
          max);
    this.constraints = constraints;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfLong(true,
                             min,
                             max,
                             constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfLong(nullable,
                                            min,
                                            max);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v -> v.isInt() || v.isLong() ?
                                                 null :
                                                 new JsError(v,
                                                             LONG_EXPECTED),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }
}
