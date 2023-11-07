package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsMapOfInt extends AbstractMap implements JsOneErrorSpec, AvroSpec {
    JsMapOfInt(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfInt(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfInt(nullable);
    }


    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value,
                    it -> !it.isInt(),
                    ERROR_CODE.INT_EXPECTED);
    }


}
