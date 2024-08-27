package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfBinary extends AbstractMap implements JsSpec, AvroSpec {

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
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> it.isBinary() ? null : ERROR_CODE.BINARY_EXPECTED
               );
  }


}
