package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class JsBooleanSpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsBooleanSpec(final boolean required,
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
        return new JsBooleanSpec(required,
                                 true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsBooleanSpec(false,
                                 nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofBool(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {

        return Functions.testElem(JsValue::isBool,
                                  BOOLEAN_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);

    }
}
