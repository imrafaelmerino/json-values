package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.FALSE_EXPECTED;

class JsFalseConstantSpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsFalseConstantSpec(final boolean required,
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
        return new JsFalseConstantSpec(required,
                                       false
        );
    }

    @Override
    public JsSpec optional() {
        return new JsFalseConstantSpec(false,
                                       nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofFalse(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testElem(JsValue::isFalse,
                                  FALSE_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);
    }
}
