package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsNumberSpec extends AbstractNullableSpec implements JsValuePredicate {
    JsNumberSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsNumberSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofNumber(nullable);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testElem(JsValue::isNumber,
                                  NUMBER_EXPECTED,
                                  nullable
                        )
                        .apply(value);

    }
}
