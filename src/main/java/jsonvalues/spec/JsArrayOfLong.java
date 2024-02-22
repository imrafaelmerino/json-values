package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

import java.util.Optional;
import jsonvalues.JsValue;

final class JsArrayOfLong extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  JsArrayOfLong(final boolean nullable) {
    super(nullable);
  }

  JsArrayOfLong(final boolean nullable,
                int min,
                int max
               ) {
    super(nullable,
          min,
          max);
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfLong(true,
                             min,
                             max);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfLong(nullable,
                                            min,
                                            max);
  }


  @Override
  public Optional<JsError> testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v -> v.isInt() || v.isLong() ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(v,
                                                                         LONG_EXPECTED)),
                                            nullable,
                                            min,
                                            max
                                           )
                    .apply(value);
  }
}
