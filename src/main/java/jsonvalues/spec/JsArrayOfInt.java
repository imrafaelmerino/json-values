package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

final class JsArrayOfInt extends AbstractSizableArr implements JsValuePredicate, JsArraySpec {


    JsArrayOfInt(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfInt(final boolean nullable,
                 int min,
                 int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfInt(true,
                                min,
                                max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfInt(nullable,
                                                   min,
                                                   max);
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("int"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isInt() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                            INT_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);

    }
}
