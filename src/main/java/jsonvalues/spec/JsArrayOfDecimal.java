package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

import java.math.BigDecimal;
import jsonvalues.JsBigDec;
import jsonvalues.JsValue;

final class JsArrayOfDecimal extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final DecimalSchemaConstraints constraints;

  JsArrayOfDecimal(final boolean nullable) {
    this(nullable,
         null);
  }

  JsArrayOfDecimal(final boolean nullable,
                   DecimalSchemaConstraints constraints,
                   ArraySchemaConstraints arrayConstraints) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;

  }

  JsArrayOfDecimal(final boolean nullable,
                   ArraySchemaConstraints arrayConstraints
                  ) {
    this(nullable,
         arrayConstraints,
         null);
  }

  JsArrayOfDecimal(final boolean nullable,
                   ArraySchemaConstraints arrayConstraints,
                   DecimalSchemaConstraints constraints
                  ) {
    super(nullable,
          arrayConstraints);
    this.constraints = constraints;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfDecimal(true,
                                arrayConstraints,
                                constraints);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDecimal(nullable,
                                               arrayConstraints,
                                               constraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v -> {
                                        if (!v.isNumber()) {
                                          return new JsError(v,
                                                             DECIMAL_EXPECTED);
                                        }
                                        if (constraints != null) {
                                          JsBigDec numericValue = v.isBigDec() ? v.toJsBigDec()
                                                                               : v.isDouble() ? JsBigDec.of(BigDecimal.valueOf(v.toJsDouble().value))
                                                                                              : v.isLong() ? JsBigDec.of(BigDecimal.valueOf(v.toJsLong().value))
                                                                                                           : v.isInt() ? JsBigDec.of(BigDecimal.valueOf(v.toJsInt().value))
                                                                                                                       : JsBigDec.of(new BigDecimal(v.toJsBigInt().value));
                                          var errorCode = Fun.testDecimalConstraints(constraints,
                                                                                      numericValue);
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
