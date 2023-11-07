package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

final class JsArrayOfInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {


    JsArrayOfInt(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfInt(final boolean nullable,
                 int min,
                 int max
                ) {
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
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfInt(nullable,
                                               min,
                                               max);
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
