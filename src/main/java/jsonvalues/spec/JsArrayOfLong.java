package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

import jsonvalues.JsValue;

final class JsArrayOfLong extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final LongSchemaConstraints constraints;

  JsArrayOfLong(final boolean nullable) {
    this(nullable,
         null,
         null);
  }


  JsArrayOfLong(boolean nullable,
                ArraySchemaConstraints arrayConstraints
               ) {
    this(nullable,
         arrayConstraints,
         null);
  }

  JsArrayOfLong(boolean nullable,
                ArraySchemaConstraints arrayConstraints,
                LongSchemaConstraints constraints
               ) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;
  }

  public JsArrayOfLong(final boolean nullable,
                       final LongSchemaConstraints build) {
    this(nullable,
         null,
         build
        );
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfLong(true,
                             arrayConstraints,
                             constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfLong(nullable,
                                            arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> v.isInt() || v.isLong() ?
                                           null :
                                           new JsError(v,
                                                       LONG_EXPECTED),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
