package jsonvalues.spec;

import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

final class JsDecimalSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
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
    public JsParser parser() {
        return JsParsers.INSTANCE.ofDecimalSuchThat(predicate,
                                                    nullable
                                                   );
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
