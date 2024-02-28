package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfStr extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final StrConstraints constraints;

  JsArrayOfStr(final boolean nullable) {
    this(nullable,
         null,
         null);
  }

  JsArrayOfStr(final boolean nullable,
               StrConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  JsArrayOfStr(final boolean nullable,
               ArraySchemaConstraints arrayConstraints
              ) {
    this(nullable,
         arrayConstraints,
         null);
  }

  JsArrayOfStr(final boolean nullable,
               ArraySchemaConstraints arrayConstraints,
               StrConstraints constraints
              ) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfStr(true,
                            arrayConstraints,
                            constraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfStr(nullable,
                                           arrayConstraints,
                                           constraints);
  }

  @Override
  public JsError testValue(final JsValue value) {
    //TODO incluir schema validation
    return Fun.testArrayOfTestedValue(v -> v.isStr() ?
                                           null :
                                           new JsError(v,
                                                       STRING_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }


}
