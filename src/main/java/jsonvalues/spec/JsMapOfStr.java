package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfStr extends AbstractMap implements JsSpec, AvroSpec {

  final StrConstraints valuesConstraints;

  JsMapOfStr(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfStr(boolean nullable,
             StrConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfStr(true,
                          valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfString(nullable,
                                            valuesConstraints);
  }

  @Override
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> {
                  if (!it.isStr()) {
                    return ERROR_CODE.STRING_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testStrConstraints(valuesConstraints,
                                                  value.toJsStr());
                  }
                  return null;
                }
               );
  }


}
