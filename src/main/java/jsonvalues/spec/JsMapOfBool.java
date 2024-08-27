package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfBool extends AbstractMap implements JsSpec, AvroSpec {

  JsMapOfBool(boolean nullable) {
    super(nullable);
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfBool(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfBool(nullable);
  }

  @Override
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> it.isBool() ? null : ERROR_CODE.BOOLEAN_EXPECTED);
  }


}
