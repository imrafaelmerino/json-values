package jsonvalues.spec;

import jsonvalues.JsValue;


final class JsMapOfStr extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  final StrConstraints valuesConstraints;

  JsMapOfStr(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfStr(boolean nullable,
             StrConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfStr(true,
                          valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfString(nullable,
                                            valuesConstraints);
  }

  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> {
                  if (!it.isStr()) {
                    return ERROR_CODE.STRING_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testStrConstraints(valuesConstraints,
                                                  value.toJsStr());
                  }
                  return null;
                }
               );


  }


}
