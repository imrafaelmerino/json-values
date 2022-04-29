package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsDecimalSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {
    final Function<BigDecimal, Optional<JsError>> predicate;

    JsDecimalSuchThatSpec(final boolean required,
                          final boolean nullable,
                          final Function<BigDecimal, Optional<JsError>> predicate
                         ) {
        super(required,
              nullable
             );
        this.predicate = predicate;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsDecimalSuchThatSpec(required,
                                         true,
                                         predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsDecimalSuchThatSpec(false,
                                         nullable,
                                         predicate
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
        final Optional<JsError> error = jsonvalues.spec.Functions.testElem(JsValue::isDecimal,
                                                                           DECIMAL_EXPECTED,
                                                                           required,
                                                                           nullable
                                                                        )
                                                                 .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsBigDec().value);
    }
}
