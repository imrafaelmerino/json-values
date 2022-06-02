package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


class JsArraySuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {

    final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate;
    private final JsArrayOfValueSpec isArray;

    JsArraySuchThatSpec(final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate,
                        final boolean nullable
    ) {
        super(nullable);
        this.isArray = new JsArrayOfValueSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArraySuchThatSpec(predicate,
                                       true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValueSuchThat(predicate,
                                                             nullable
        );
    }

    @Override
    public Optional<Pair<JsValue,ERROR_CODE>> test(final JsValue value) {
        final Optional<Pair<JsValue,ERROR_CODE>> result = isArray.test(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
