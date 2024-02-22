package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

import java.util.Optional;
import java.util.function.Function;
import jsonvalues.JsValue;

final class JsBinarySuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final Function<byte[], Optional<JsError>> predicate;

  JsBinarySuchThat(final Function<byte[], Optional<JsError>> predicate,
                   final boolean nullable
                  ) {
    super(nullable);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsBinarySuchThat(predicate,
                                true
    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofBinarySuchThat(predicate,
                                               nullable
                                              );
  }


  @Override
  public Optional<JsError> testValue(final JsValue value) {
    Optional<JsError> error = Functions.testElem(JsValue::isBinary,
                                                 BINARY_EXPECTED,
                                                 nullable
                                                )
                                       .apply(value);

    return error.isPresent() || value.isNull() ?
           error :
           predicate.apply(value.toJsBinary().value);
  }
}
