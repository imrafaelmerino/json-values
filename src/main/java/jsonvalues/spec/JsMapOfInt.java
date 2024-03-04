package jsonvalues.spec;

import jsonvalues.JsValue;


final class JsMapOfInt extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  final IntegerSchemaConstraints valuesConstraints;

  JsMapOfInt(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfInt(boolean nullable,
             IntegerSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfInt(true,
                          valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfInt(nullable,
                                         valuesConstraints);
  }


  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> {
                  if(!it.isInt())return ERROR_CODE.INT_EXPECTED;
                  if(valuesConstraints != null) {
                    return Fun.testIntConstraints(valuesConstraints,
                                                   value.toJsInt());
                  }
                  return null;
                }
               );

  }


}
