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

  JsArrayOfTestedDecimal(final Function<BigDecimal, JsError> predicate,
                         final boolean nullable,
                         int min,
                         int max
                        ) {
    super(nullable,
          min,
          max);
    this.predicate = predicate;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfTestedDecimal(predicate,
                                      true,
                                      min,
                                      max
    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDecimalEachSuchThat(predicate,
                                                           nullable,
                                                           min,
                                                           max
                                                          );
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v ->
                                                v.isDouble() || v.isBigDec() ?
                                                predicate.apply(v.toJsBigDec().value) :
                                                new JsError(v,
                                                            DECIMAL_EXPECTED

                                                ),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }


}
