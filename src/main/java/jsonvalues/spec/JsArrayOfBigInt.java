package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

final class JsArrayOfBigInt extends AbstractSizableArr implements JsValuePredicate, JsArraySpec {
    JsArrayOfBigInt(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfBigInt(final boolean nullable,
                    int min,
                    int max) {
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
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE
                .ofArrayOfIntegral(nullable,
                                   min,
                                   max);
    }

    @Override
    public JsValue toAvro() {
        JsObj items = JsObj.of("type", JsStr.of("string"),
                                "logicalType", JsStr.of("biginteger"));

        JsObj schema = JsObj.of("type",JsStr.of("array"),
                                "items",items);

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(v,
                                                                            INTEGRAL_EXPECTED
                                                        )),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
