package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

final class JsBigIntSpec extends AbstractNullable implements JsValuePredicate,AvroSpec {

    JsBigIntSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsBigIntSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntegral(nullable);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isIntegral,
                                  INTEGRAL_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }
}
