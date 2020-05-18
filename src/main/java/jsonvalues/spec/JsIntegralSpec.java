package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsIntegralSpec extends AbstractPredicateSpec implements JsValuePredicate {

    JsIntegralSpec(final boolean required,
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
        return new JsIntegralSpec(required,
                                  true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsIntegralSpec(false,
                                  nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntegral(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testElem(JsValue::isIntegral,
                                  INTEGRAL_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);

    }
}
