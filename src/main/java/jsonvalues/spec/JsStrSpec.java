package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

final class JsStrSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    JsStrSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsStrSpec(true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofStr(nullable);
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
