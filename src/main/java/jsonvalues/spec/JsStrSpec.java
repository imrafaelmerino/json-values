package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import java.util.Optional;
import jsonvalues.JsValue;

final class JsStrSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  JsStrSpec(final boolean nullable) {
    super(nullable);
  }


  @Override
  public JsSpec nullable() {
    return new JsStrSpec(true);
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofStr(nullable);
  }


  @Override
  public Optional<JsError> testValue(final JsValue value) {
    return Functions.testElem(JsValue::isStr,
                              STRING_EXPECTED,
                              nullable
                             )
                    .apply(value);
  }
}
