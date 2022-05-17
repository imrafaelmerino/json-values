package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsArrayOfTestedLongSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final LongFunction<Optional<JsError>> predicate;

    JsArrayOfTestedLongSpec(final LongFunction<Optional<JsError>> predicate,
                            final boolean nullable
    ) {
        super(
              nullable
        );
        this.predicate = predicate;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedLongSpec(predicate,
                                           true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLongEachSuchThat(predicate,
                                                                nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                {
                                                    if (v.isLong() || v.isInt())
                                                        return predicate.apply(v.toJsLong().value);
                                                    else return Optional.of(new JsError(v,
                                                                                        LONG_EXPECTED
                                                                            )
                                                    );
                                                },
                                                nullable
                        )
                        .apply(value);
    }
}
