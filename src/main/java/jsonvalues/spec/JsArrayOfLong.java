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
                                            arrayConstraints,
                                            constraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> {
                                        if (!v.isInt() && !v.isLong()) {
                                          return new JsError(v,
                                                             LONG_EXPECTED);
                                        }
                                        if (constraints != null) {
                                          var longValue = v.isLong() ? v.toJsLong()
                                                                     : jsonvalues.JsLong.of(v.toJsInt().value);
                                          var errorCode = Fun.testLongConstraints(constraints,
                                                                                   longValue);
                                          if (errorCode != null) {
                                            return new JsError(v,
                                                               errorCode);
                                          }
                                        }
                                        return null;
                                      },
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
