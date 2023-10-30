package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

final class JsNumberSpec extends AbstractNullable implements JsValuePredicate {
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
    public JsValue toAvro() {
        return
                nullable ? JsArray.of("null", "double") : JsStr.of("double") ;
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
