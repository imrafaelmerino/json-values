package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfLongSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfLongSpec isArrayOfLong;

    JsArrayOfLongSuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                              final boolean required,
                              final boolean nullable
                             ) {
        super(required,
              nullable
             );
        this.isArrayOfLong = new JsArrayOfLongSpec(required,
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
        return new JsArrayOfLongSuchThatSpec(predicate,
                                             required,
                                             true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfLongSuchThatSpec(predicate,
                                             false,
                                             nullable
        );

    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLongSuchThat(predicate,
                                                            nullable
                                                           );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> result = isArrayOfLong.test(value);
        if (result.isPresent() || value.isNull()) return result;
        return predicate.apply(value.toJsArray());
    }
}
