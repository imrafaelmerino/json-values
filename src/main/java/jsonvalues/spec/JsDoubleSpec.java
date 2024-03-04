package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

import jsonvalues.JsValue;

final class JsDoubleSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final DoubleSchemaConstraints constraints;

  JsDoubleSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  JsDoubleSpec(boolean nullable,
               DoubleSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsDoubleSpec(true,
                            constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofDouble(nullable,
                                       constraints);
  }

  @Override
  public JsError testValue(final JsValue value) {
    var error =
        Fun.testValue(JsValue::isDouble,
                      DOUBLE_EXPECTED,
                      nullable,
                      value
                     );
    if (error != null) {
      return error;
    }

    if (constraints != null) {
      var errorCode = Fun.testDoubleConstraints(constraints,
                                                value.toJsDouble());
      if (errorCode != null) {
        return new JsError(value,
                           errorCode);
      }
    }

    return null;
  }


}
