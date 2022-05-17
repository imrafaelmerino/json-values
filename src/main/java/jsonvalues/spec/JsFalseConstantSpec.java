package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.FALSE_EXPECTED;

class JsFalseConstantSpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsFalseConstantSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsFalseConstantSpec(false);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofFalse(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isFalse,
                                  FALSE_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }
}
