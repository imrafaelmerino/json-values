package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

import java.util.Optional;
import jsonvalues.JsValue;

final class JsBigIntSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  JsBigIntSpec(final boolean nullable) {
    super(nullable);
  }


  @Override
  public JsSpec nullable() {
    return new JsBigIntSpec(true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofIntegral(nullable);
  }


  @Override
  public Optional<JsError> testValue(final JsValue value) {
    return Functions.testElem(JsValue::isIntegral,
                              INTEGRAL_EXPECTED,
                              nullable
                             )
                    .apply(value);

  }
}
