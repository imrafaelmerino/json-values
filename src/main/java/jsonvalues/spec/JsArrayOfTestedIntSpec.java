package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsArrayOfTestedIntSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    final IntFunction<Optional<Error>> predicate;

    JsArrayOfTestedIntSpec(final IntFunction<Optional<Error>> predicate,
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
        return new JsArrayOfTestedIntSpec(predicate,
                                          required,
                                          true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedIntSpec(predicate,
                                          false,
                                          nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntEachSuchThat(predicate,
                                                               nullable
                                                              );
    }

    @Override
    public Optional<Error> test(final JsValue value) {

        return Functions.testArrayOfTestedValue(v ->
                                                {
                                                    if (v.isInt()) return predicate.apply(v.toJsInt().value);
                                                    else return Optional.of(new Error(v,
                                                                                      INT_EXPECTED
                                                                            )
                                                                           );
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
