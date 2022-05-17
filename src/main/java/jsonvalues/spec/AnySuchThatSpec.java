package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class AnySuchThatSpec implements JsValuePredicate {

    private final Function<JsValue, Optional<JsError>> predicate;

    AnySuchThatSpec(final Function<JsValue, Optional<JsError>> predicate
    ) {
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return this;
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofValueSuchThat(predicate);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {

        if (value.isNothing())
            return Optional.of(new JsError(value,
                                           ERROR_CODE.REQUIRED));
        return predicate.apply(value);

    }
}
