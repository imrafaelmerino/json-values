package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

class JsBinarySpec extends AbstractNullableSpec implements JsValuePredicate {
    JsBinarySpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsBinarySpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofBinary(nullable);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testElem(JsValue::isBinary,
                                  BINARY_EXPECTED,
                                  nullable
                        )
                        .apply(value);

    }


}
