package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsMapOfBool extends AbstractMap implements JsOneErrorSpec, AvroSpec {
    JsMapOfBool(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBool(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfBool(nullable);
    }


    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value, it -> !it.isBool(), ERROR_CODE.BOOLEAN_EXPECTED);
    }


}
