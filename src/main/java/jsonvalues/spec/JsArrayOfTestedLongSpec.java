package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsArrayOfTestedLongSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final LongFunction<Optional<Error>> predicate;

    JsArrayOfTestedLongSpec(final LongFunction<Optional<Error>> predicate,
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
        return new JsArrayOfTestedLongSpec(predicate,
                                           required,
                                           true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedLongSpec(predicate,
                                           false,
                                           nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLongEachSuchThat(predicate,
                                                                nullable
                                                               );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                {
                                                    if (v.isLong() || v.isInt())
                                                        return predicate.apply(v.toJsLong().value);
                                                    else return Optional.of(new Error(v,
                                                                                      LONG_EXPECTED
                                                                            )
                                                                           );
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
