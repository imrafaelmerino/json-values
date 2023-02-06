package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

class JsInstantSpec extends AbstractNullableSpec implements JsValuePredicate {
    JsInstantSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsInstantSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofInstant(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isInstant,
                                  INSTANT_EXPECTED,
                                  nullable
                        )
                        .apply(value);

    }


}
