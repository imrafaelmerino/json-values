package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

import jsonvalues.JsValue;

final class JsLongSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final LongSchemaConstraints constraints;

  JsLongSpec(final boolean nullable) {
    this(nullable,
         null);
  }

  JsLongSpec(final boolean nullable,
             final LongSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsLongSpec(true,
                          constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofLong(nullable,
                                     constraints);
  }

  @Override
  public JsError testValue(final JsValue value) {
    var error =
        Fun.testValue(JsValue::isLong,
                      LONG_EXPECTED,
                      nullable,
                      value
                     );
    if (error != null) {
      return error;
    }

    if (constraints != null) {
      var errorCode = Fun.testLongConstraints(constraints,
                                              value.toJsLong());
      if (errorCode != null) {
        return new JsError(value,
                           errorCode);
      }
    }

    return null;
  }


}
