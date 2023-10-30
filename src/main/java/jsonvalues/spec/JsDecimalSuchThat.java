package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

final class JsDecimalSuchThat extends AbstractNullable implements JsValuePredicate {
    final Function<BigDecimal, Optional<JsError>> predicate;

    JsDecimalSuchThat(final Function<BigDecimal, Optional<JsError>> predicate,
                      final boolean nullable
                     ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsDecimalSuchThat(predicate,
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
    public JsValue toAvro() {
        return nullable ? JsArray.of("null", "double") : JsStr.of("double");
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
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
