package jsonvalues.spec;

import java.util.Optional;
import jsonvalues.JsValue;


final class JsMapOfDouble extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  JsMapOfDouble(boolean nullable) {
    super(nullable);
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfDouble(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfDouble(nullable);
  }


  @Override
  public Optional<JsError> testValue(JsValue value) {
    return test(value,
                it -> !it.isDouble(),
                ERROR_CODE.DOUBLE_EXPECTED);
  }

}
