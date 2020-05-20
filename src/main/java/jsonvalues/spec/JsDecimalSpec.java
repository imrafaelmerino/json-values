package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsDecimalSpec extends AbstractPredicateSpec implements JsValuePredicate {

    JsDecimalSpec(final boolean required,
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
        return new JsDecimalSpec(required,
                                 true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsDecimalSpec(false,
                                 nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofDecimal(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testElem(JsValue::isDecimal,
                                  DECIMAL_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);

    }
}
