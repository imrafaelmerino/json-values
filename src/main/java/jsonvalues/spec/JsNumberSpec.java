package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

final class JsNumberSpec extends AbstractNullable implements JsValuePredicate, AvroSpec {
    JsNumberSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsNumberSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofNumber(nullable);
    }

    @Override
    public JsValue toAvroSchema() {
        return
                nullable ? JsArray.of("null", "double","int","long") : JsArray.of("double","int","long") ;
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
