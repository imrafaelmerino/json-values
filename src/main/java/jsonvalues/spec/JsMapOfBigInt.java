package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsMapOfBigInt extends AbstractMap implements JsOneErrorSpec, AvroSpec {
    JsMapOfBigInt(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBigInt(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfBigInt(nullable);
    }


    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value, it -> !it.isIntegral(), ERROR_CODE.INTEGRAL_EXPECTED);
    }


}
