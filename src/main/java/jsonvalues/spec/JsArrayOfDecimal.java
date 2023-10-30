package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

final class JsArrayOfDecimal extends AbstractSizableArr implements JsValuePredicate, JsArraySpec {

    JsArrayOfDecimal(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfDecimal(final boolean nullable,
                     int min,
                     int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfDecimal(true,
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
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("double"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
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
