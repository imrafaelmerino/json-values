package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsMapOfDec extends AbstractMap implements JsOneErrorSpec, AvroSpec {
    JsMapOfDec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfDec(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfDecimal(nullable);
    }


    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value, it -> !it.isNumber(), ERROR_CODE.DECIMAL_EXPECTED);
    }


}
