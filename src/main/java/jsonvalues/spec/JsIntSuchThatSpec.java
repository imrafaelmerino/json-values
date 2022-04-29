package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsIntSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {
    final IntFunction<Optional<JsError>> predicate;

    JsIntSuchThatSpec(final boolean required,
                      final boolean nullable,
                      final IntFunction<Optional<JsError>> predicate
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
        return new JsIntSuchThatSpec(required,
                                     true,
                                     predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsIntSuchThatSpec(false,
                                     nullable,
                                     predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntSuchThat(predicate,
                                                    nullable
                                                   );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> error = jsonvalues.spec.Functions.testElem(JsValue::isInt,
                                                                           INT_EXPECTED,
                                                                           required,
                                                                           nullable
                                                                        )
                                                                 .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsInt().value);
    }
}
