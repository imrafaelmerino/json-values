package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfBigIntSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate;
    private final JsArrayOfBigIntSpec isArrayOfIntegral;

    JsArrayOfBigIntSuchThatSpec(final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate,
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
    public Optional<Pair<JsValue,ERROR_CODE>> testValue(final JsValue value) {
        final Optional<Pair<JsValue,ERROR_CODE>> result = isArrayOfIntegral.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
