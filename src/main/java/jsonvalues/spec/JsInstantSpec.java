package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

import jsonvalues.JsValue;

final class JsInstantSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  InstantSchemaConstraints constraints;

  JsInstantSpec(final boolean nullable,
                final InstantSchemaConstraints constraints) {
    super(nullable);
    this.constraints = constraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsInstantSpec(true,
                             null);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofInstant(nullable,
                                        constraints);
  }

  @Override
  public JsError testValue(final JsValue value) {
    var error =
        Fun.testValue(JsValue::isInstant,
                      INSTANT_EXPECTED,
                      nullable,
                      value
                     );
    if (error != null) {
      return error;
    }

    if (constraints != null) {
      var errorCode = Fun.testInstantConstraints(constraints,
                                                 value.toJsInstant());
      if (errorCode != null) {
        return new JsError(value,
                           errorCode);
      }
    }

    return null;


  }


}
