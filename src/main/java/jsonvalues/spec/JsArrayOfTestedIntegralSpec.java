package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsArrayOfTestedIntegralSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    private final Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsArrayOfTestedIntegralSpec(final Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedIntegralSpec(final Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
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
        return new JsArrayOfTestedIntegralSpec(predicate,
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
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                        predicate.apply(v.toJsBigInt().value) :
                                                        Optional.of(Pair.of(v,
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
