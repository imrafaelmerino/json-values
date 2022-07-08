package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
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
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isObj,
                                  OBJ_EXPECTED,
                                  nullable)
                        .apply(value);

    }
}
