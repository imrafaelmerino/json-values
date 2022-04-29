package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.TRUE_EXPECTED;

class JsTrueConstantSpec extends AbstractPredicateSpec implements JsValuePredicate {

    JsTrueConstantSpec(final boolean required,
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
        return new JsTrueConstantSpec(required,
                                      true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsTrueConstantSpec(false,
                                      nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofTrue(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isTrue,
                                  TRUE_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);
    }
}
