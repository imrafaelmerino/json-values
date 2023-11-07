package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

final class JsArrayOfObj extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {
    JsArrayOfObj(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfObj(final boolean nullable,
                 int min,
                 int max
                ) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfObj(true,
                                min,
                                max);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfObj(nullable,
                                               min,
                                               max);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isObj() ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(v,
                                                                                OBJ_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }
}
