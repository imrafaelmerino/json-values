package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsInstantSpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsInstantSpec(final boolean required,
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
        return new JsInstantSpec(required,
                                 true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsInstantSpec(false,
                                 nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofInstant(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testElem(JsValue::isInstant,
                                  INSTANT_EXPECTED,
                                  required,
                                  nullable
                                 )
                        .apply(value);

    }


}
