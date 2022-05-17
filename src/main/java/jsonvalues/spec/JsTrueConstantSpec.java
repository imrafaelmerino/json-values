package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.TRUE_EXPECTED;

class JsTrueConstantSpec extends AbstractPredicateSpec implements JsValuePredicate {

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
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isTrue,
                                  TRUE_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }
}
