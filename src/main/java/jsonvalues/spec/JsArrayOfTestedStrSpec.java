package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsArrayOfTestedStrSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    final Function<String, Optional<Error>> predicate;

    JsArrayOfTestedStrSpec(final Function<String, Optional<Error>> predicate,
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
        return new JsArrayOfTestedStrSpec(predicate,
                                          required,
                                          true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedStrSpec(predicate,
                                          false,
                                          nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStrEachSuchThat(predicate,
                                                               nullable
                                                              );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                {
                                                    if (v.isStr()) return predicate.apply(v.toJsStr().value);
                                                    else return Optional.of(new Error(v,
                                                                                      STRING_EXPECTED
                                                                            )
                                                                           );
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
