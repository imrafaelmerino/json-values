package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import jsonvalues.JsValue;

final class JsStrSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final StrConstraints constraints;

  JsStrSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  public JsStrSpec(final boolean nullable,
                   final StrConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsStrSpec(true,
                         constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofStr(nullable);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return constraints == null ?
           Functions.testValue(JsValue::isStr,
                               STRING_EXPECTED,
                               nullable,
                               value
                              ) :
           Functions.testStrConstraints(constraints,
                                        value)
        ;
  }
}
