package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

final class JsStrSpec extends AbstractNullable implements JsValuePredicate, AvroSpec {
    JsStrSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsStrSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStr(nullable);
    }

    @Override
    public JsValue toAvroSchema() {

        return nullable ?
                JsArray.of("null", "string") :
                JsStr.of("string");
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isStr,
                                  STRING_EXPECTED,
                                  nullable
                                 )
                        .apply(value);
    }
}
