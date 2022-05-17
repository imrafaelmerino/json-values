package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsArrayOfLongSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfLongSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfLongSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLong(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isInt() || v.isLong() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             LONG_EXPECTED)),
                                                nullable
                        )
                        .apply(value);
    }
}
