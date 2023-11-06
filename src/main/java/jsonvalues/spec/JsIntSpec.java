package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

final class JsIntSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    JsIntSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsIntSpec(true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofInt(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isInt,
                                  INT_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }



}
