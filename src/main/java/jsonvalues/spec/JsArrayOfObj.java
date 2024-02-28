package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfObj extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {

  JsArrayOfObj(final boolean nullable) {
    super(nullable);
  }

  JsArrayOfObj(final boolean nullable,
               ArraySchemaConstraints arrayConstraints
              ) {
    super(nullable,
          arrayConstraints);
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfObj(true,
                            arrayConstraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfObj(nullable,
                                           arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> v.isObj() ?
                                           null :
                                           new JsError(v,
                                                       OBJ_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
