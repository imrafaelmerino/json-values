package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsMapOfLong extends AbstractMap implements JsOneErrorSpec, AvroSpec {
    JsMapOfLong(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfLong(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfLong(nullable);
    }


    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value, it -> !it.isLong() && !it.isInt(), ERROR_CODE.LONG_EXPECTED);
    }

}
