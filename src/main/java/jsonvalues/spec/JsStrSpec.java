package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsStrSpec extends AbstractNullableSpec implements JsValuePredicate {
    JsStrSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsStrSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStr(nullable);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testElem(JsValue::isStr,
                                  STRING_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }
}
