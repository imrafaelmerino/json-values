package jsonvalues.spec;

import jsonvalues.JsValue;


final class JsMapOfBigInt extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  final BigIntSchemaConstraints valuesConstraints;

  JsMapOfBigInt(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfBigInt(boolean nullable,
                BigIntSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfBigInt(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfBigInt(nullable);
  }


  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> !it.isIntegral(),
                ERROR_CODE.INTEGRAL_EXPECTED);
  }


}
