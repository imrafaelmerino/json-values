package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

import java.util.Optional;
import java.util.function.IntFunction;
import jsonvalues.JsValue;

final class JsIntSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final IntFunction<Optional<JsError>> predicate;

  JsIntSuchThat(final IntFunction<Optional<JsError>> predicate,
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
  public Optional<JsError> testValue(final JsValue value) {
    final Optional<JsError> error = Functions.testElem(JsValue::isInt,
                                                       INT_EXPECTED,
                                                       nullable
                                                      )
                                             .apply(value);

    return error.isPresent() || value.isNull() ?
           error :
           predicate.apply(value.toJsInt().value);
  }
}
