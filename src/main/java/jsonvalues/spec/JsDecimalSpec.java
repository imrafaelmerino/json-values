package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

import jsonvalues.JsValue;

final class JsDecimalSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final DecimalSchemaConstraints constraints;

  JsDecimalSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  JsDecimalSpec(final boolean nullable,
                final DecimalSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsDecimalSpec(true,
                             constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofDecimal(nullable,
                                        constraints
                                       );
  }

  @Override
  public JsError testValue(final JsValue value) {
    var error =
        Fun.testValue(JsValue::isNumber,
                      DECIMAL_EXPECTED,
                      nullable,
                      value
                     );
    if (error != null) {
      return error;
    }

    if (constraints != null) {
      var errorCode = Fun.testDecimalConstraints(constraints,
                                                 value.toJsBigDec());
      if (errorCode != null) {
        return new JsError(value,
                           errorCode);
      }
    }

    return null;

  }


}
