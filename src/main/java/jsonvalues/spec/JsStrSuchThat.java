package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import java.util.function.Function;
import jsonvalues.JsValue;

final class JsStrSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final Function<String, JsError> predicate;

  JsStrSuchThat(final Function<String, JsError> predicate,
                final boolean nullable
               ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsStrSuchThat(predicate,
                             true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofStrSuchThat(predicate,
                                            nullable
                                           );
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError error = Fun.testValue(JsValue::isStr,
                                        STRING_EXPECTED,
                                        nullable,
                                        value
                                       );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsStr().value);
  }
}
