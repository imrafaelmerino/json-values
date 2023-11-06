package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsMapOfInstant extends AbstractMap implements JsOneErrorSpec, AvroSpec {
    JsMapOfInstant(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfInstant(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfInstant(nullable);
    }


    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value,
                    it -> !it.isInstant(),
                    ERROR_CODE.INSTANT_EXPECTED);
    }


}
