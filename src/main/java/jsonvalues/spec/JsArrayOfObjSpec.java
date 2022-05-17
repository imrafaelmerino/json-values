package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

class JsArrayOfObjSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfObjSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfObjSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfObj(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isObj() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             OBJ_EXPECTED)),
                                                nullable
                        )
                        .apply(value);
    }
}
