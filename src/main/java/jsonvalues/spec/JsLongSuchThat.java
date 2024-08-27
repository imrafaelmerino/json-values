package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

import java.util.function.LongFunction;
import jsonvalues.JsValue;

final class JsLongSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final LongFunction<JsError> predicate;

  JsLongSuchThat(final LongFunction<JsError> predicate,
                 final boolean nullable
                ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsLongSuchThat(
        predicate,
        true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofLongSuchThat(predicate,
                                             nullable
                                            );
  }


  @Override
  public JsError testValue(final JsValue value) {
    JsError error =
        Fun.testValue(JsValue::isLong,
                      LONG_EXPECTED,
                      nullable,
                      value
                     );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsLong().value);
  }
}
