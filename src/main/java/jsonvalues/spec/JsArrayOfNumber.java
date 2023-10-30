package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

final class JsArrayOfNumber extends AbstractSizableArr implements JsValuePredicate, JsArraySpec {
    JsArrayOfNumber(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfNumber(final boolean nullable,
                    int min,
                    int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfNumber(true,
                                   min,
                                   max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumber(nullable,
                                                      min,
                                                      max);
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsArray.of("int","long","double"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isNumber() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                            NUMBER_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
