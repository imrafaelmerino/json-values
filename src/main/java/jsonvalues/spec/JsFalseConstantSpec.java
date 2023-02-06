package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.FALSE_EXPECTED;

class JsFalseConstantSpec extends AbstractNullableSpec implements JsValuePredicate {
    JsFalseConstantSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsFalseConstantSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofFalse(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isFalse,
                                  FALSE_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }
}
