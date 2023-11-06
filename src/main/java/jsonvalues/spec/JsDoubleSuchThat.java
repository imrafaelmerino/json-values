package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.DoubleFunction;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

final class JsDoubleSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    final DoubleFunction<Optional<JsError>> predicate;

    JsDoubleSuchThat(final DoubleFunction<Optional<JsError>> predicate,
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
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isDouble,
                                                           DOUBLE_EXPECTED,
                                                           nullable
                                                          )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsDouble().value);
    }
}
