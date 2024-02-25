package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

import java.math.BigInteger;
import java.util.function.Function;
import jsonvalues.JsValue;

final class JsBigIntSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final Function<BigInteger, JsError> predicate;

  JsBigIntSuchThat(final Function<BigInteger, JsError> predicate,
                   final boolean nullable
                  ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsBigIntSuchThat(
        predicate,
        true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofIntegralSuchThat(predicate,
                                                 nullable
                                                );
  }


  @Override
  public JsError testValue(final JsValue value) {
    JsError error =
        Fun.testValue(JsValue::isIntegral,
                      INTEGRAL_EXPECTED,
                      nullable,
                      value
                     );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsBigInt().value);
  }
}
