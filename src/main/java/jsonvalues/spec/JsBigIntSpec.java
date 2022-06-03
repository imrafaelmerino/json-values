package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsBigIntSpec extends AbstractNullableSpec implements JsValuePredicate {

    JsBigIntSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsBigIntSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntegral(nullable);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testElem(JsValue::isIntegral,
                                  INTEGRAL_EXPECTED,
                                  nullable
                        )
                        .apply(value);

    }
}
