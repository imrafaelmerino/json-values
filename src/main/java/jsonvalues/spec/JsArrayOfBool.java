package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

final class JsArrayOfBool extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    JsArrayOfBool(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfBool(final boolean nullable,
                  int min,
                  int max
                 ) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfBool(true,
                                 min,
                                 max);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfBool(nullable,
                                                min,
                                                max);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isBool() ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(v,
                                                                                BOOLEAN_EXPECTED)),
                                                nullable,
                                                min,
                                                max)
                        .apply(value);
    }
}
