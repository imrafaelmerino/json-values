package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsDecimalSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {
    final Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsDecimalSuchThatSpec(final Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                          final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsDecimalSuchThatSpec(predicate,
                                         true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofDecimalSuchThat(predicate,
                                                        nullable
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        final Optional<Pair<JsValue, ERROR_CODE>> error = Functions.testElem(JsValue::isDecimal,
                                                                             DECIMAL_EXPECTED,
                                                                             nullable
                                                                   )
                                                                   .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsBigDec().value);
    }
}
