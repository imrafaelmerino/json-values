package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

import java.util.function.DoubleFunction;
import jsonvalues.JsValue;

final class JsDoubleSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final DoubleFunction<JsError> predicate;

  JsDoubleSuchThat(final DoubleFunction<JsError> predicate,
                   final boolean nullable
                  ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsDoubleSuchThat(predicate,
                                true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofDoubleSuchThat(predicate,
                                               nullable
                                              );
  }


  @Override
  public JsError testValue(final JsValue value) {
    final JsError error = Fun.testValue(JsValue::isDouble,
                                        DOUBLE_EXPECTED,
                                        nullable,
                                        value
                                       );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsDouble().value);
  }
}
