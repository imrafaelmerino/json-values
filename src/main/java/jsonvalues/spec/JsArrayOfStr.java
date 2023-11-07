package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

final class JsArrayOfStr extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    JsArrayOfStr(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfStr(final boolean nullable,
                 int min,
                 int max
                ) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfStr(true,
                                min,
                                max);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfStr(nullable,
                                               min,
                                               max);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isStr() ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(v,
                                                                                STRING_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }


}
