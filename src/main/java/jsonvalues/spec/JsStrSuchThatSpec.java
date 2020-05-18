package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsStrSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {


    final Function<String, Optional<Error>> predicate;

    JsStrSuchThatSpec(final boolean required,
                      final boolean nullable,
                      final Function<String, Optional<Error>> predicate
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
        return new JsStrSuchThatSpec(required,
                                     true,
                                     predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsStrSuchThatSpec(false,
                                     nullable,
                                     predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStrSuchThat(predicate,
                                                    nullable
                                                   );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isStr,
                                                                         STRING_EXPECTED,
                                                                         required,
                                                                         nullable
                                                                        )
                                                               .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsStr().value);
    }
}
