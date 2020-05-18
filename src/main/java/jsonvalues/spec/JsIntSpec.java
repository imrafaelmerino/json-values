package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsIntSpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsIntSpec(final boolean required,
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
        return new JsIntSpec(required,
                             true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsIntSpec(false,
                             nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofInt(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testElem(JsValue::isInt,
                                  INT_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);

    }
}
