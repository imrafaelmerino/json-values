package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.TRUE_EXPECTED;

final class JsTrueConstant extends AbstractNullable implements JsValuePredicate, AvroSpec {

    JsTrueConstant(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsTrueConstant(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofTrue(nullable);
    }

    @Override
    public JsValue toAvroSchema() {
        return nullable ?
                JsArray.of("null", "boolean") :
                JsStr.of("boolean");
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isTrue,
                                  TRUE_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }
}
