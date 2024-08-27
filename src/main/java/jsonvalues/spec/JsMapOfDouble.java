package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfDouble extends AbstractMap implements JsSpec, AvroSpec {

  final DoubleSchemaConstraints valuesConstraints;

  JsMapOfDouble(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfDouble(boolean nullable,
                DoubleSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfDouble(true,
                             valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfDouble(nullable,
                                            valuesConstraints);
  }

  @Override
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> {
                  if (!it.isDouble()) {
                    return ERROR_CODE.DOUBLE_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testDoubleConstraints(valuesConstraints,
                                                     value.toJsDouble());
                  }
                  return null;
                }
               );
  }


}
