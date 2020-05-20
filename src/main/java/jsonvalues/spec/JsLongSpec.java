package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsLongSpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsLongSpec(final boolean required,
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
        return new JsLongSpec(required,
                              true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsLongSpec(false,
                              nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofLong(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testElem(JsValue::isLong,
                                  LONG_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);
    }
}
