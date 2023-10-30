package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

final class JsLongSpec extends AbstractNullable implements JsValuePredicate {
    JsLongSpec(final boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsLongSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofLong(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isLong,
                                  LONG_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }

    @Override
    public JsValue toAvro() {
        return
                nullable ? JsArray.of("null", "long") : JsStr.of("long") ;    }
}
