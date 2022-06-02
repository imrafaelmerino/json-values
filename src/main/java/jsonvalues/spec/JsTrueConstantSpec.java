package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.TRUE_EXPECTED;

class JsTrueConstantSpec extends AbstractNullableSpec implements JsValuePredicate {

    JsTrueConstantSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsTrueConstantSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofTrue(nullable);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testElem(JsValue::isTrue,
                                  TRUE_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }
}
