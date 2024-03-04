package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import jsonvalues.JsValue;

final class JsStrSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final StrConstraints constraints;

  JsStrSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  JsStrSpec(final boolean nullable,
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
    return JsParsers.INSTANCE.ofStr(nullable,
                                    constraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    var error =
        Fun.testValue(JsValue::isStr,
                      STRING_EXPECTED,
                      nullable,
                      value
                     );
    if (error != null) {
      return error;
    }

    if (constraints != null) {
      var errorCode = Fun.testStrConstraints(constraints,
                                             value.toJsStr());
      if (errorCode != null) {
        return new JsError(value,
                           errorCode);
      }
    }

    return null;
  }

}
