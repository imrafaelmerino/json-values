package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfBool extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  JsArrayOfBool(final boolean nullable) {
    super(nullable);
  }

  JsArrayOfBool(final boolean nullable,
                int min,
                int max
               ) {
    super(nullable,
          min,
          max);
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfBool(true,
                             min,
                             max);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfBool(nullable,
                                            min,
                                            max);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v -> v.isBool() ?
                                                 null :
                                                 new JsError(v,
                                                             BOOLEAN_EXPECTED),
                                            nullable,
                                            min,
                                            max,
                                            value);
  }
}
