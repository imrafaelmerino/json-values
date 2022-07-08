package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class JsBooleanSpec extends AbstractNullableSpec implements JsValuePredicate {
    JsBooleanSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsBooleanSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofBool(nullable);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {

        return Functions.testElem(JsValue::isBool,
                                  BOOLEAN_EXPECTED,
                                  nullable
                        )
                        .apply(value);

    }
}
