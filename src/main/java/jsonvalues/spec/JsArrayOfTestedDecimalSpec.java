package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsArrayOfTestedDecimalSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    final Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsArrayOfTestedDecimalSpec(final Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                               final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedDecimalSpec(final Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
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
        return new JsArrayOfTestedDecimalSpec(predicate,
                                              true,
                                              min,
                                              max
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimalEachSuchThat(predicate,
                                                                   nullable,
                                                                   min,
                                                                   max
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isDouble() || v.isBigDec() ?
                                                        predicate.apply(v.toJsBigDec().value) :
                                                        Optional.of(Pair.of(v,
                                                                               DECIMAL_EXPECTED
                                                                    )
                                                        ),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
