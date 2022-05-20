package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsDecimalSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {
    final Function<BigDecimal, Optional<JsError>> predicate;

    JsDecimalSuchThatSpec(final Function<BigDecimal, Optional<JsError>> predicate,
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
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isDecimal,
                                                           DECIMAL_EXPECTED,
                                                           nullable
                                                 )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsBigDec().value);
    }
}
