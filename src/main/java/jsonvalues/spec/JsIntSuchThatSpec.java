package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsIntSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {
    final IntFunction<Optional<JsError>> predicate;

    JsIntSuchThatSpec(final IntFunction<Optional<JsError>> predicate,
                      final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsIntSuchThatSpec(predicate,
                                     true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntSuchThat(predicate,
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
