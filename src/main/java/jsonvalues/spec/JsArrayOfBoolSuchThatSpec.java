package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


class JsArrayOfBoolSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {

    private final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate;
    private final JsArrayOfBoolSpec isArrayOfBool;

    JsArrayOfBoolSuchThatSpec(final Function<JsArray, Optional<Pair<JsValue,ERROR_CODE>>> predicate,
                              final boolean nullable
    ) {
        super(nullable);
        this.isArrayOfBool = new JsArrayOfBoolSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfBoolSuchThatSpec(predicate,
                                             true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfBoolSuchThat(predicate,
                                                            nullable);
    }

    @Override
    public Optional<Pair<JsValue,ERROR_CODE>> testValue(final JsValue value) {
        final Optional<Pair<JsValue,ERROR_CODE>> result = isArrayOfBool.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());

    }
}
