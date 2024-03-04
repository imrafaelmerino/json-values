package jsonvalues.spec;

import jsonvalues.JsValue;


final class JsMapOfDec extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  final DecimalSchemaConstraints valuesConstraints;

  JsMapOfDec(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfDec(boolean nullable,
             DecimalSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfDec(true,
                          valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfDecimal(nullable,
                                             valuesConstraints);
  }


  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> {
                  if (!it.isNumber()) {
                    return ERROR_CODE.DECIMAL_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testDecimalConstraints(valuesConstraints,
                                                      value.toJsBigDec());
                  }
                  return null;
                }
               );
  }


}
