package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

import java.time.Instant;
import java.util.function.Function;
import jsonvalues.JsValue;

final class JsInstantSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final Function<Instant, JsError> predicate;

  JsInstantSuchThat(final Function<Instant, JsError> predicate,
                    final boolean nullable
                   ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsInstantSuchThat(predicate,
                                 true
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofInstantSuchThat(predicate,
                                                nullable
                                               );
  }


  @Override
  public JsError testValue(final JsValue value) {
    JsError error =
        Fun.testValue(JsValue::isInstant,
                      INSTANT_EXPECTED,
                      nullable,
                      value
                     );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsInstant().value);
  }
}
