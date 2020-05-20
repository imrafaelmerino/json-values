package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsArrayOfStrSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfStrSpec(final boolean required,
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
        return new JsArrayOfStrSpec(required,
                                    true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfStrSpec(false,
                                    nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStr(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> {
                                                    if (v.isStr()) return Optional.empty();
                                                    else return Optional.of(new Error(v,
                                                                                      STRING_EXPECTED));
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
