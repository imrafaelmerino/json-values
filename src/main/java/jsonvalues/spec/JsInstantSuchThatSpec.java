package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

class JsInstantSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {

    final Function<Instant, Optional<JsError>> predicate;

    JsInstantSuchThatSpec(final Function<Instant, Optional<JsError>> predicate,
                          final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsInstantSuchThatSpec(predicate,
                                         true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofInstantSuchThat(predicate,
                                                        nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isInstant,
                                                           INSTANT_EXPECTED,
                                                           nullable
                                                 )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsInstant().value);
    }
}
