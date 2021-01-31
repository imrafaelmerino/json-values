package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

class JsInstantSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {

    final Function<Instant, Optional<Error>> predicate;

    JsInstantSuchThatSpec(final boolean required,
                          final boolean nullable,
                          final Function<Instant, Optional<Error>> predicate
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
        return new JsInstantSuchThatSpec(required,
                                         true,
                                         predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsInstantSuchThatSpec(false,
                                         nullable,
                                         predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofInstantSuchThat(predicate,
                                                        nullable
                                                       );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> error = Functions.testElem(JsValue::isInstant,
                                                         INSTANT_EXPECTED,
                                                         required,
                                                         nullable
                                                        )
                                               .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsInstant().value);
    }
}
