package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsBigIntSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {

    final Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsBigIntSuchThatSpec(final Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                         final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsBigIntSuchThatSpec(
                predicate,
                true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntegralSuchThat(predicate,
                                                         nullable
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        final Optional<Pair<JsValue, ERROR_CODE>> error = jsonvalues.spec.Functions.testElem(JsValue::isIntegral,
                                                                                             INTEGRAL_EXPECTED,
                                                                                             nullable
                                                                    )
                                                                                   .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsBigInt().value);
    }
}
