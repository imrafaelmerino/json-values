package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

final class JsArrayOfLong extends AbstractSizableArr implements JsValuePredicate, JsArraySpec {
    JsArrayOfLong(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfLong(final boolean nullable,
                  int min,
                  int max) {
        super(nullable,
              min,
              max);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfLong(true,
                                 min,
                                 max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLong(nullable,
                                                    min,
                                                    max);
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("long"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isInt() || v.isLong() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                            LONG_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
