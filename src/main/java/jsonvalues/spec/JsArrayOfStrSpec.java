package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsArrayOfStrSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfStrSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfStrSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStr(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isStr() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             STRING_EXPECTED)),
                                                nullable
                        )
                        .apply(value);
    }
}
