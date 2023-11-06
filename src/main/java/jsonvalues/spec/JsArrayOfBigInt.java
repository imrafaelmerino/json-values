package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

final class JsArrayOfBigInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    JsArrayOfBigInt(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfBigInt(final boolean nullable,
                    int min,
                    int max
                   ) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfBigInt(true,
                                   min,
                                   max);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE
                .ofArrayOfIntegral(nullable,
                                   min,
                                   max);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                                Optional.empty() :
                                                                Optional.of(new JsError(v,
                                                                                        INTEGRAL_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }
}
