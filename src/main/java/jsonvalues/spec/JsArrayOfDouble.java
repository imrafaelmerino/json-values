package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfDouble extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final DoubleSchemaConstraints constraints;

  JsArrayOfDouble(final boolean nullable) {
    this(nullable,
         null,
         null);
  }

  JsArrayOfDouble(boolean nullable,
                  DoubleSchemaConstraints constraints,
                  ArraySchemaConstraints arrayConstraints) {
    super(nullable);
    this.constraints = constraints;
    this.arrayConstraints = arrayConstraints;
  }

  JsArrayOfDouble(final boolean nullable,
                  ArraySchemaConstraints arrayConstraints
                 ) {
    this(nullable,
         null,
         arrayConstraints);
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfDouble(true,
                               constraints,
                               arrayConstraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDouble(nullable,
                                              arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> v.isDouble() ?
                                           null :
                                           new JsError(v,
                                                       DOUBLE_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
