package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsLongSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {
    final LongFunction<Optional<Error>> predicate;

    JsLongSuchThatSpec(final boolean required,
                       final boolean nullable,
                       final LongFunction<Optional<Error>> predicate
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
        return new JsLongSuchThatSpec(required,
                                      true,
                                      predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsLongSuchThatSpec(false,
                                      nullable,
                                      predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofLongSuchThat(predicate,
                                                     nullable
                                                    );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isLong,
                                                                         LONG_EXPECTED,
                                                                         required,
                                                                         nullable
                                                                        )
                                                               .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsLong().value);
    }
}
