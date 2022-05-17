package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsArrayOfNumberSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfNumberSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfNumberSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumber(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isNumber() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             NUMBER_EXPECTED)),
                                                nullable
                        )
                        .apply(value);
    }
}
