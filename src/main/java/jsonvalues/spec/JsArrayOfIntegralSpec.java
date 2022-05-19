package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsArrayOfIntegralSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfIntegralSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfIntegralSpec(final boolean nullable,
                          int min,
                          int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfIntegralSpec(true,
                                         min,
                                         max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE
                .ofArrayOfIntegral(nullable,
                                   min,
                                   max);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(v,
                                                                                INTEGRAL_EXPECTED
                                                        )),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
