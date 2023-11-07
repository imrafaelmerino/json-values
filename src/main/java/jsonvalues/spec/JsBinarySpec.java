package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

final class JsBinarySpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    JsBinarySpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsBinarySpec(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofBinary(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isBinary,
                                  BINARY_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }


}
