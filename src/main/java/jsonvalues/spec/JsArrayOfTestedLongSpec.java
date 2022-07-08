package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsArrayOfTestedLongSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    private final LongFunction<Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsArrayOfTestedLongSpec(final LongFunction<Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                            final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedLongSpec(final LongFunction<Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                            final boolean nullable,
                            int min,
                            int max
    ) {
        super(nullable,
              min,
              max);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedLongSpec(predicate,
                                           true,
                                           min,
                                           max
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLongEachSuchThat(predicate,
                                                                nullable,
                                                                min,
                                                                max
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isLong() || v.isInt() ?
                                                        predicate.apply(v.toJsLong().value) :
                                                        Optional.of(Pair.of(v,
                                                                               LONG_EXPECTED
                                                                    )
                                                        ),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
