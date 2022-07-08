package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfLongSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate;
    private final JsArrayOfLongSpec isArrayOfLong;

    JsArrayOfLongSuchThatSpec(final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate,
                              final boolean nullable
    ) {
        super(nullable);
        this.isArrayOfLong = new JsArrayOfLongSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfLongSuchThatSpec(predicate,
                                             true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLongSuchThat(predicate,
                                                            nullable
        );
    }

    @Override
    public Optional<Pair<JsValue,ERROR_CODE>> testValue(final JsValue value) {
        Optional<Pair<JsValue,ERROR_CODE>> result = isArrayOfLong.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
