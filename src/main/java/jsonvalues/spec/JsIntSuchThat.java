package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

import java.util.function.IntFunction;
import jsonvalues.JsValue;

final class JsIntSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final IntFunction<JsError> predicate;

  JsIntSuchThat(final IntFunction<JsError> predicate,
                final boolean nullable
               ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsIntSuchThat(predicate,
                             true
    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofIntSuchThat(predicate,
                                            nullable
                                           );
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError error = Fun.testValue(JsValue::isInt,
                                        INT_EXPECTED,
                                        nullable,
                                        value
                                       );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsInt().value);
  }
}
