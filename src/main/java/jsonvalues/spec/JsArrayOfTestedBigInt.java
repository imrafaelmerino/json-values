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

  JsArrayOfTestedBigInt(Function<BigInteger, JsError> predicate,
                        boolean nullable,
                        ArraySchemaConstraints arrayConstraints
                       ) {
    super(nullable,
          arrayConstraints);
    this.predicate = predicate;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfTestedBigInt(predicate,
                                     true,
                                     arrayConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfIntegralEachSuchThat(predicate,
                                                            nullable,
                                                            arrayConstraints
                                                           );
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v ->
                                          v.isIntegral() ?
                                          predicate.apply(v.toJsBigInt().value) :
                                          new JsError(v,
                                                      INTEGRAL_EXPECTED

                                          ),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }


}
