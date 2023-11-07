package jsonvalues.spec;

import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

final class JsArrayOfTestedBigInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    private final Function<BigInteger, Optional<JsError>> predicate;

    JsArrayOfTestedBigInt(final Function<BigInteger, Optional<JsError>> predicate,
                          final boolean nullable
                         ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedBigInt(final Function<BigInteger, Optional<JsError>> predicate,
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
        return new JsArrayOfTestedBigInt(predicate,
                                         true,
                                         min,
                                         max);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfIntegralEachSuchThat(predicate,
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
