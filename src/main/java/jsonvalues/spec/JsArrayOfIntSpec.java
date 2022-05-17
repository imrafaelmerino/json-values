package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsArrayOfIntSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {


    JsArrayOfIntSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfIntSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfInt(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isInt() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             INT_EXPECTED)),
                                                nullable
                        )
                        .apply(value);

    }
}
