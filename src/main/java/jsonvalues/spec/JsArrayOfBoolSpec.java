package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class JsArrayOfBoolSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfBoolSpec(final boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfBoolSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfBool(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isBool() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             BOOLEAN_EXPECTED)),
                                                nullable)
                        .apply(value);
    }
}
