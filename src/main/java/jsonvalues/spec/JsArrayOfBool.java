package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfBool extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  JsArrayOfBool(final boolean nullable) {
    super(nullable);
  }

  JsArrayOfBool(final boolean nullable,
                ArraySchemaConstraints arrayConstraints
               ) {
    super(nullable,
          arrayConstraints);
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfBool(true,
                             arrayConstraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfBool(nullable,
                                            arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> v.isBool() ?
                                           null :
                                           new JsError(v,
                                                       BOOLEAN_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value);
  }
}
