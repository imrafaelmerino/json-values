package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfDec extends AbstractMap implements JsSpec, AvroSpec {

  final DecimalSchemaConstraints valuesConstraints;

  JsMapOfDec(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfDec(boolean nullable,
             DecimalSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfDec(true,
                          valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfDecimal(nullable,
                                             valuesConstraints);
  }

  @Override
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> {
                  if (!it.isNumber()) {
                    return ERROR_CODE.DECIMAL_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testDecimalConstraints(valuesConstraints,
                                                      value.toJsBigDec());
                  }
                  return null;
                }
               );
  }


}
