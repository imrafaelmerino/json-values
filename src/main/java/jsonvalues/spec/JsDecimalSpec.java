package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

import java.util.Optional;
import jsonvalues.JsValue;

final class JsDecimalSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  JsDecimalSpec(final boolean nullable) {
    super(nullable);
  }


  @Override
  public JsSpec nullable() {
    return new JsDecimalSpec(true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofDecimal(nullable);
  }

  @Override
  public Optional<JsError> testValue(final JsValue value) {
    return Functions.testElem(JsValue::isNumber,
                              DECIMAL_EXPECTED,
                              nullable
                             )
                    .apply(value);

  }


}
