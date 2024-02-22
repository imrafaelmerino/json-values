package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

import java.util.Optional;
import jsonvalues.JsValue;

final class JsArrayOfDecimal extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  JsArrayOfDecimal(final boolean nullable) {
    super(nullable);
  }

  JsArrayOfDecimal(final boolean nullable,
                   int min,
                   int max
                  ) {
    super(nullable,
          min,
          max);
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfDecimal(true,
                                min,
                                max);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDecimal(nullable,
                                               min,
                                               max);
  }


  @Override
  public Optional<JsError> testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v -> v.isNumber() ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(v,
                                                                         DECIMAL_EXPECTED)),
                                            nullable,
                                            min,
                                            max
                                           )
                    .apply(value);
  }
}
