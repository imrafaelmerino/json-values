package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfLong extends AbstractMap implements JsSpec, AvroSpec {

  final LongSchemaConstraints valuesConstraints;

  JsMapOfLong(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfLong(boolean nullable,
              LongSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfLong(true,
                           valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfLong(nullable,
                                          valuesConstraints);
  }

  @Override
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> {
                  if (!it.isLong() && !it.isInt()) {
                    return ERROR_CODE.LONG_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testLongConstraints(valuesConstraints,
                                                   value.toJsLong());
                  }
                  return null;
                }
               );
  }


}
