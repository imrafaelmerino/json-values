package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class AnySuchThatSpec implements JsValuePredicate {

    private final boolean required;
    private final Function<JsValue, Optional<JsError>> predicate;

    AnySuchThatSpec(final boolean required,
                    final Function<JsValue, Optional<JsError>> predicate
                   ) {
        this.required = required;
        this.predicate = predicate;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return this;
    }

    @Override
    public JsSpec optional() {
        return new AnySuchThatSpec(false,
                                   predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofValueSuchThat(predicate);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {

        if (value.isNothing() && required) return Optional.of(new JsError(value,
                                                                          ERROR_CODE.REQUIRED));
        return predicate.apply(value);

    }
}
