package jsonvalues.spec;

import java.util.Optional;
import jsonvalues.JsValue;


final class JsMapOfBinary extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  JsMapOfBinary(boolean nullable) {
    super(nullable);
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfBinary(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfBinary(nullable);
  }

  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> !it.isBinary(),
                ERROR_CODE.BINARY_EXPECTED);

  }


}
