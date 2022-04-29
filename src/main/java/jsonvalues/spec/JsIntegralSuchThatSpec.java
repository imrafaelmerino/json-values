package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsIntegralSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {

    final Function<BigInteger, Optional<JsError>> predicate;

    JsIntegralSuchThatSpec(final boolean required,
                           final boolean nullable,
                           final Function<BigInteger, Optional<JsError>> predicate
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
        return new JsIntegralSuchThatSpec(required,
                                          true,
                                          predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsIntegralSuchThatSpec(false,
                                          nullable,
                                          predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntegralSuchThat(predicate,
                                                         nullable
                                                        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> error = jsonvalues.spec.Functions.testElem(JsValue::isIntegral,
                                                                           INTEGRAL_EXPECTED,
                                                                           required,
                                                                           nullable
                                                                        )
                                                                 .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsBigInt().value);
    }
}
