package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfBigIntSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfBigIntSpec isArrayOfIntegral;

    JsArrayOfBigIntSuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                                final boolean nullable
    ) {
        super(nullable);
        this.isArrayOfIntegral = new JsArrayOfBigIntSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfBigIntSuchThatSpec(predicate,
                                               true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntegralSuchThat(predicate,
                                                                nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArrayOfIntegral.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
