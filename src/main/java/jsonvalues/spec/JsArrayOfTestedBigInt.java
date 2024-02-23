package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

import java.math.BigInteger;
import java.util.function.Function;
import jsonvalues.JsValue;

final class JsArrayOfTestedBigInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  private final Function<BigInteger, JsError> predicate;

  JsArrayOfTestedBigInt(final Function<BigInteger, JsError> predicate,
                        final boolean nullable
                       ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedBigInt(final Function<BigInteger, JsError> predicate,
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
    return new JsArrayOfTestedBigInt(predicate,
                                     true,
                                     min,
                                     max);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfIntegralEachSuchThat(predicate,
                                                            nullable,
                                                            min,
                                                            max
                                                           );
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v ->
                                                v.isIntegral() ?
                                                predicate.apply(v.toJsBigInt().value) :
                                                new JsError(v,
                                                            INTEGRAL_EXPECTED

                                                ),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }


}
