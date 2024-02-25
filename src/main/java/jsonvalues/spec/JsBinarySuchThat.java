package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

import java.util.function.Function;
import jsonvalues.JsValue;

final class JsBinarySuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final Function<byte[], JsError> predicate;

  JsBinarySuchThat(final Function<byte[], JsError> predicate,
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
  public JsError testValue(final JsValue value) {
    JsError error = Fun.testValue(JsValue::isBinary,
                                  BINARY_EXPECTED,
                                  nullable,
                                  value
                                 );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsBinary().value);
  }
}
