package jsonvalues.spec;

import java.util.Optional;
import jsonvalues.JsValue;


final class JsMapOfStr extends AbstractMap implements JsOneErrorSpec, AvroSpec {

  final StrConstraints schema;

  JsMapOfStr(boolean nullable) {
    this(nullable,
         null);
  }

  JsMapOfStr(boolean nullable,
             StrConstraints schema) {
    super(nullable);
    this.schema = schema;
  }

  @Override
  public JsSpec nullable() {
    return new JsMapOfStr(true,
                          schema);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofMapOfString(nullable,schema);
  }

  @Override
  public Optional<JsError> testValue(JsValue value) {
    //todo incluir schema validation
    return test(value,
                it -> !it.isStr(),
                ERROR_CODE.STRING_EXPECTED);

  }


}
