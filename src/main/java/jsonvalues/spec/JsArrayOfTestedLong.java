package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

final class JsArrayOfTestedLong extends AbstractSizableArr implements JsValuePredicate, JsArraySpec, AvroSpec {
    private final LongFunction<Optional<JsError>> predicate;

    JsArrayOfTestedLong(final LongFunction<Optional<JsError>> predicate,
                        final boolean nullable
                       ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedLong(final LongFunction<Optional<JsError>> predicate,
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
        return new JsArrayOfTestedLong(predicate,
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
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isLong() || v.isInt() ?
                                                        predicate.apply(v.toJsLong().value) :
                                                        Optional.of(new JsError(v,
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
