package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

final class JsDecimalSpec extends AbstractNullable implements JsValuePredicate, AvroSpec {

    JsDecimalSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsDecimalSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofDecimal(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isDecimal,
                                  DECIMAL_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }



}
