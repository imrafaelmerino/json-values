package jsonvalues.spec;

import java.util.Optional;
import jsonvalues.JsValue;


final class JsMapOfStr extends AbstractMap implements JsOneErrorSpec, AvroSpec {

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
  public JsError testValue(JsValue value) {
    //todo incluir schema validation
    return test(value,
                it -> !it.isStr(),
                ERROR_CODE.STRING_EXPECTED);

  }


}
