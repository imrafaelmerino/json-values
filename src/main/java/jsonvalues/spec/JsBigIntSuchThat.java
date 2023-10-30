package jsonvalues.spec;

import jsonvalues.*;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

final class JsBigIntSuchThat extends AbstractNullable implements JsValuePredicate {

    final Function<BigInteger, Optional<JsError>> predicate;

    JsBigIntSuchThat(final Function<BigInteger, Optional<JsError>> predicate,
                     final boolean nullable
                    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsBigIntSuchThat(
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
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("string"),
                                "logicalType", JsStr.of("biginteger"));
        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> error = jsonvalues.spec.Functions.testElem(JsValue::isIntegral,
                                                                           INTEGRAL_EXPECTED,
                                                                           nullable
                                                                          )
                                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsBigInt().value);
    }
}
