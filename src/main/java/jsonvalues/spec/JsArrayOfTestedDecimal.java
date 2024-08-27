package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

import java.math.BigDecimal;
import java.util.function.Function;
import jsonvalues.JsValue;

final class JsArrayOfTestedDecimal extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<BigDecimal, JsError> predicate;

  JsArrayOfTestedDecimal(final Function<BigDecimal, JsError> predicate,
                         final boolean nullable
                        ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedDecimal(Function<BigDecimal, JsError> predicate,
                         boolean nullable,
                         ArraySchemaConstraints arrayConstraints
                        ) {
    super(nullable,
          arrayConstraints);
    this.predicate = predicate;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfTestedDecimal(predicate,
                                      true,
                                      arrayConstraints
    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDecimalEachSuchThat(predicate,
                                                           nullable,
                                                           arrayConstraints
                                                          );
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v ->
                                          v.isDouble() || v.isBigDec() ?
                                          predicate.apply(v.toJsBigDec().value) :
                                          new JsError(v,
                                                      DECIMAL_EXPECTED

                                          ),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }


}
