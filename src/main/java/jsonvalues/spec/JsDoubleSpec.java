package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

import java.util.Optional;
import jsonvalues.JsValue;

final class JsDoubleSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  JsDoubleSpec(final boolean nullable) {
    super(nullable);
  }

  @Override
  public JsSpec nullable() {
    return new JsDoubleSpec(true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofDouble(nullable);
  }

  @Override
  public Optional<JsError> testValue(final JsValue value) {
    return Functions.testElem(JsValue::isDouble,
                              DOUBLE_EXPECTED,
                              nullable
                             )
                    .apply(value);
  }


}
