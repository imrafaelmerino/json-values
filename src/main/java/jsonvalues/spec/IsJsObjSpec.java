package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

final class IsJsObjSpec extends AbstractNullable implements JsValuePredicate {
    IsJsObjSpec(final boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new IsJsObjSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofObj(nullable);
    }

    @Override
    public JsValue toAvro() {
        return null;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isObj,
                                  OBJ_EXPECTED,
                                  nullable)
                        .apply(value);

    }
}
