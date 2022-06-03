package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsArrayOfBigIntSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfBigIntSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfBigIntSpec(final boolean nullable,
                        int min,
                        int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfBigIntSpec(true,
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
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                        Optional.empty() :
                                                        Optional.of(Pair.of(v,
                                                                               INTEGRAL_EXPECTED
                                                        )),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
