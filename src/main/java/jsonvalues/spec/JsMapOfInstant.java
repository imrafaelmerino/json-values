package jsonvalues.spec;

import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


final class JsMapOfInstant extends AbstractMap implements JsSpec, AvroSpec {

  final InstantSchemaConstraints valuesConstraints;

  JsMapOfInstant(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfInstant(boolean nullable,
                 InstantSchemaConstraints valuesConstraints) {
    super(nullable);
    this.valuesConstraints = valuesConstraints;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfInstant(true,
                              valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfInstant(nullable,
                                             valuesConstraints);
  }

  @Override
  public List<SpecError> test(final JsPath parentPath,
                              final JsValue value) {
    return test(parentPath,
                value,
                it -> {
                  if (!it.isInstant()) {
                    return ERROR_CODE.INSTANT_EXPECTED;
                  }
                  if (valuesConstraints != null) {
                    return Fun.testInstantConstraints(valuesConstraints,
                                                      value.toJsInstant());
                  }
                  return null;
                }
               );
  }


}
