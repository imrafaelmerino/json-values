package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

final class JsArrayOfDouble extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

    JsArrayOfDouble(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfDouble(final boolean nullable,
                    int min,
                    int max
                   ) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfDouble(true,
                                   min,
                                   max);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfDouble(nullable,
                                                  min,
                                                  max);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isDouble() ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(v,
                                                                                DOUBLE_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }
}
