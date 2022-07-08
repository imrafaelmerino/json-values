package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfDecimalSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {

    private final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> predicate;
    private final JsArrayOfDecimalSpec isArrayOfDecimal;

    JsArrayOfDecimalSuchThatSpec(final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                 final boolean nullable
    ) {
        super(nullable);
        this.isArrayOfDecimal = new JsArrayOfDecimalSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfDecimalSuchThatSpec(predicate,
                                                true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimalSuchThat(predicate,
                                                               nullable);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        final Optional<Pair<JsValue, ERROR_CODE>> result = isArrayOfDecimal.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
