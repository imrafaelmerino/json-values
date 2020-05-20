package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsArrayOfTestedNumberSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsNumber, Optional<Error>> predicate;

    JsArrayOfTestedNumberSpec(final Function<JsNumber, Optional<Error>> predicate,
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
        return new JsArrayOfTestedNumberSpec(predicate,
                                             required,
                                             true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedNumberSpec(predicate,
                                             false,
                                             nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumberEachSuchThat(predicate,
                                                                  nullable
                                                                 );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                {
                                                    if (v.isNumber()) return predicate.apply(v.toJsNumber());
                                                    else return Optional.of(new Error(v,
                                                                                      NUMBER_EXPECTED
                                                                            )
                                                                           );
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
