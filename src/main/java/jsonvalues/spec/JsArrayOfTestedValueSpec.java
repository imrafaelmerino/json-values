package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfTestedValueSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsValue, Optional<JsError>> predicate;

    JsArrayOfTestedValueSpec(final Function<JsValue, Optional<JsError>> predicate,
                             final boolean required,
                             final boolean nullable
                            ) {
        super(required,
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
        return new JsArrayOfTestedValueSpec(predicate,
                                            required,
                                            true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedValueSpec(predicate,
                                            false,
                                            nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValueEachSuchThat(predicate,
                                                                 nullable
                                                                );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(predicate,
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
