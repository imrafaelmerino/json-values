package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

final class IsJsObj extends AbstractNullable implements JsOneErrorSpec {
    IsJsObj(final boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new IsJsObj(true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofObj(nullable);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isObj,
                                  OBJ_EXPECTED,
                                  nullable)
                        .apply(value);

    }
}
