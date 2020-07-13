package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


class JsArraySuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {

    final Function<JsArray, Optional<Error>> predicate;
    private final JsArrayOfValueSpec isArray;

    JsArraySuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                        final boolean required,
                        final boolean nullable
                       ) {
        super(required,
              nullable
             );
        this.isArray = new JsArrayOfValueSpec(required,
                                              nullable
        );
        this.predicate = predicate;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsArraySuchThatSpec(predicate,
                                       required,
                                       true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArraySuchThatSpec(predicate,
                                       false,
                                       nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValueSuchThat(predicate,
                                                             nullable
                                                            );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> result = isArray.test(value);
        if (result.isPresent() || value.isNull()) return result;
        return predicate.apply(value.toJsArray());
    }
}
