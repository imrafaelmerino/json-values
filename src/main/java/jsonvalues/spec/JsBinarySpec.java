package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

class JsBinarySpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsBinarySpec(final boolean required,
                 final boolean nullable
                ) {
        super(required,
              nullable
             );
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsBinarySpec(required,
                                true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsBinarySpec(false,
                                nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofBinary(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isBinary,
                                  BINARY_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);

    }


}
