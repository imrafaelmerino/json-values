package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

final class JsLongSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    JsLongSpec(final boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsLongSpec(true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofLong(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isLong,
                                  LONG_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }


}
