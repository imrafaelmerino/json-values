package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.FALSE_EXPECTED;

final class JsFalseConstant extends AbstractNullable implements JsValuePredicate {
    JsFalseConstant(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsFalseConstant(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofFalse(nullable);
    }

    @Override
    public JsValue toAvro() {
        return nullable ?
                JsArray.of("null", "boolean") :
                JsStr.of("boolean");
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isFalse,
                                  FALSE_EXPECTED,
                                  nullable
                                 )
                        .apply(value);
    }
}
