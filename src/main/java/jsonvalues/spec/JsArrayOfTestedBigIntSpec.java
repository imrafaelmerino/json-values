package jsonvalues.spec;

import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsArrayOfTestedBigIntSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    private final Function<BigInteger, Optional<JsError>> predicate;

    JsArrayOfTestedBigIntSpec(final Function<BigInteger, Optional<JsError>> predicate,
                              final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedBigIntSpec(final Function<BigInteger, Optional<JsError>> predicate,
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
        return new JsArrayOfTestedBigIntSpec(predicate,
                                             true,
                                             min,
                                             max);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntegralEachSuchThat(predicate,
                                                                    nullable,
                                                                    min,
                                                                    max
        );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                        predicate.apply(v.toJsBigInt().value) :
                                                        Optional.of(new JsError(v,
                                                                            INTEGRAL_EXPECTED
                                                                    )
                                                        ),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
