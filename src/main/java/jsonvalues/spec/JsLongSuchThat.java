package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

final class JsLongSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    final LongFunction<Optional<JsError>> predicate;

    JsLongSuchThat(final LongFunction<Optional<JsError>> predicate,
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
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isLong,
                                                           LONG_EXPECTED,
                                                           nullable
                                                          )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsLong().value);
    }
}
