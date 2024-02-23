package jsonvalues.spec;

import java.util.Optional;
import jsonvalues.JsValue;


final class JsMapOfDouble extends AbstractMap implements JsOneErrorSpec, AvroSpec {

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
    return new JsMapOfDouble(true,valuesConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfDouble(nullable);
  }


  @Override
  public JsError testValue(JsValue value) {
    return test(value,
                it -> !it.isDouble(),
                ERROR_CODE.DOUBLE_EXPECTED);
  }

}
