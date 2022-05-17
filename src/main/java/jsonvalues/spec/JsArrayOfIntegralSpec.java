package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsArrayOfIntegralSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfIntegralSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfIntegralSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntegral(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(v,
                                                                                INTEGRAL_EXPECTED
                                                        )),
                                                nullable
                        )
                        .apply(value);
    }
}
