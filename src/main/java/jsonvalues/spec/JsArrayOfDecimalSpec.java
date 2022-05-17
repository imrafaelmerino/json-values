package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsArrayOfDecimalSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {

    JsArrayOfDecimalSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfDecimalSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimal(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isDecimal() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             DECIMAL_EXPECTED)),
                                                nullable
                        )
                        .apply(value);
    }
}
