package jsonvalues.spec;

import java.util.Optional;
import jsonvalues.JsValue;


final class JsMapOfInstant extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  JsMapOfInstant(boolean nullable) {
    super(nullable);
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfInstant(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfInstant(nullable);
  }


  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> !it.isInstant(),
                ERROR_CODE.INSTANT_EXPECTED);
  }


}
