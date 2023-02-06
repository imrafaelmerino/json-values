package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsArrayOfDecimalSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {

    JsArrayOfDecimalSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfDecimalSpec(final boolean nullable,
                         int min,
                         int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfDecimalSpec(true,
                                        min,
                                        max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimal(nullable,
                                                       min,
                                                       max);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isDecimal() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                            DECIMAL_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
