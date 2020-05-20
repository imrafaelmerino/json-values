package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsNumberSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {
    final Function<JsNumber, Optional<Error>> predicate;

    JsNumberSuchThatSpec(final boolean required,
                         final boolean nullable,
                         final Function<JsNumber, Optional<Error>> predicate
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
        return new JsNumberSuchThatSpec(required,
                                        true,
                                        predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsNumberSuchThatSpec(false,
                                        nullable,
                                        predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofNumberSuchThat(predicate,
                                                       nullable
                                                      );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isNumber,
                                                                         NUMBER_EXPECTED,
                                                                         required,
                                                                         nullable
                                                                        )
                                                               .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsNumber());
    }
}
