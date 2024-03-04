package jsonvalues.spec;

import jsonvalues.JsValue;


final class JsMapOfInstant extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  final InstantSchemaConstraints valuesConstraints;

  JsMapOfInstant(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfInstant(boolean nullable,
                 InstantSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfInstant(true,
                              valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfInstant(nullable,
                                             valuesConstraints);
  }


  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> {
                  if (!it.isInstant() ) {
                    return ERROR_CODE.INSTANT_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testInstantConstraints(valuesConstraints,
                                                      value.toJsInstant());
                  }
                  return null;
                }
               );
  }


}
