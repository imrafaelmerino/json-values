package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;


class JsArrayOfValueSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {

    JsArrayOfValueSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfValueSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValue(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArray(nullable)
                        .apply(value);
    }
}
