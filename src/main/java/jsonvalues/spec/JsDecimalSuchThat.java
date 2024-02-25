package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

import java.math.BigDecimal;
import java.util.function.Function;
import jsonvalues.JsValue;

final class JsDecimalSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final Function<BigDecimal, JsError> predicate;

  JsDecimalSuchThat(final Function<BigDecimal, JsError> predicate,
                    final boolean nullable
                   ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsDecimalSuchThat(predicate,
                                 true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofDecimalSuchThat(predicate,
                                                nullable
                                               );
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError error =
        Fun.testValue(JsValue::isDecimal,
                      DECIMAL_EXPECTED,
                      nullable,
                      value
                     );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsBigDec().value);
  }
}
