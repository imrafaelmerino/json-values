package jsonvalues.spec;

import jsonvalues.JsValue;


final class JsArrayOfValue extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {

  JsArrayOfValue(final boolean nullable) {
    super(nullable);
  }

  JsArrayOfValue(boolean nullable,
                 ArraySchemaConstraints arrayConstraints
                ) {
    super(nullable,
          arrayConstraints);
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfValue(true,
                              arrayConstraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfValue(nullable,
                                             arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArray(nullable,
                         arrayConstraints,
                         value);
  }

}
