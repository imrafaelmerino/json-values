package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

final class JsNumberSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    JsNumberSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsNumberSpec(true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofNumber(nullable);
    }



    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isNumber,
                                  NUMBER_EXPECTED,
                                  nullable
                        )
                        .apply(value);

    }
}
