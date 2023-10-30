package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

final class JsBinarySpec extends AbstractNullable implements JsValuePredicate {
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
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isBinary,
                                  BINARY_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }


    @Override
    public JsValue toAvro() {
        return nullable ? JsArray.of("null", "bytes") : JsStr.of("bytes");
    }


}
