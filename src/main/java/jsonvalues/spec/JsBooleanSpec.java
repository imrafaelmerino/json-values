package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

final class JsBooleanSpec extends AbstractNullable implements JsOneErrorSpec, AvroSpec {
    JsBooleanSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsBooleanSpec(true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofBool(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {

        return Functions.testElem(JsValue::isBool,
                                  BOOLEAN_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }


}
