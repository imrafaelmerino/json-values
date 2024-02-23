package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import java.util.Optional;
import jsonvalues.JsValue;

final class JsArrayOfStr extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final StrConstraints schema;

  JsArrayOfStr(final boolean nullable) {
    this(nullable,
         null);
  }

  JsArrayOfStr(final boolean nullable,
               StrConstraints schema) {
    super(nullable);
    this.schema = schema;
  }

  JsArrayOfStr(final boolean nullable,
               int min,
               int max
              ) {
    this(nullable,
         min,
         max,
         null);
  }

  JsArrayOfStr(final boolean nullable,
               int min,
               int max,
               StrConstraints schema
              ) {
    super(nullable,
          min,
          max);
    this.schema = schema;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfStr(true,
                            min,
                            max,
                            schema);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfStr(nullable,
                                           min,
                                           max,
                                           schema);
  }

  @Override
  public Optional<JsError> testValue(final JsValue value) {
    //TODO incluir schema validation
    return Functions.testArrayOfTestedValue(v -> v.isStr() ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(v,
                                                                         STRING_EXPECTED)),
                                            nullable,
                                            min,
                                            max
                                           )
                    .apply(value);
  }


}
