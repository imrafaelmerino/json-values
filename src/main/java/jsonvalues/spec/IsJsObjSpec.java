package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

class IsJsObjSpec extends AbstractNullableSpec implements JsValuePredicate {
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
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isObj,
                                  OBJ_EXPECTED,
                                  nullable)
                        .apply(value);

    }
}
