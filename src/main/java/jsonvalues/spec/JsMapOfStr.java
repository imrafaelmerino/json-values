package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsMapOfStr extends AbstractMap implements JsOneErrorSpec, AvroSpec {
    JsMapOfStr(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfStr(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfString(nullable);
    }

    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value, it -> !it.isStr(), ERROR_CODE.STRING_EXPECTED);

    }


}
