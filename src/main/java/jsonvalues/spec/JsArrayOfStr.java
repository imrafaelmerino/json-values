package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfStr extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final StrConstraints constraints;

  JsArrayOfStr(final boolean nullable) {
    this(nullable,
         null);
  }

  JsArrayOfStr(final boolean nullable,
               StrConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  JsArrayOfStr(final boolean nullable,
               int min,
               int max
              ) {
    this(nullable,
         min,
         max,
         null);
  }

  JsArrayOfStr(final boolean nullable,
               int min,
               int max,
               StrConstraints constraints
              ) {
    super(nullable,
          min,
          max);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfStr(true,
                            min,
                            max,
                            constraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfStr(nullable,
                                           min,
                                           max,
                                           constraints);
  }

  @Override
  public JsError testValue(final JsValue value) {
    //TODO incluir schema validation
    return Functions.testArrayOfTestedValue(v -> v.isStr() ?
                                                 null :
                                                 new JsError(v,
                                                             STRING_EXPECTED),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }


}
