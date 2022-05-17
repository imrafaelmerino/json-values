package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsIntSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {
    final IntFunction<Optional<JsError>> predicate;

    JsIntSuchThatSpec(final boolean nullable,
                      final IntFunction<Optional<JsError>> predicate
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsIntSuchThatSpec(true,
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
        final Optional<JsError> error = Functions.testElem(JsValue::isInt,
                                                           INT_EXPECTED,
                                                           nullable
                                                 )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsInt().value);
    }
}
