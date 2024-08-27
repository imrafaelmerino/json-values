package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfBigInt extends AbstractMap implements JsSpec, AvroSpec {

  final BigIntSchemaConstraints valuesConstraints;

  JsMapOfBigInt(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfBigInt(boolean nullable,
                BigIntSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfBigInt(true);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfBigInt(nullable,
                                            valuesConstraints);
  }

  @Override
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> {
                  if (!it.isIntegral()) {
                    return ERROR_CODE.INTEGRAL_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testBigIntConstraints(valuesConstraints,
                                                     value.toJsBigInt());
                  }
                  return null;
                }
               );
  }


}
