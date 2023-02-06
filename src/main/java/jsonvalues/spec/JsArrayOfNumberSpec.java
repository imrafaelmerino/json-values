package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsArrayOfNumberSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfNumberSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfNumberSpec(final boolean nullable,
                        int min,
                        int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfNumberSpec(true,
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
