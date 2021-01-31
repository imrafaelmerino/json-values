package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsArrayOfNumberSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfNumberSpec(final boolean required,
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
        return new JsArrayOfNumberSpec(required,
                                       true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfNumberSpec(false,
                                       nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumber(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> {
                                                    if (v.isNumber()) return Optional.empty();
                                                    else return Optional.of(new Error(v,
                                                                                      NUMBER_EXPECTED));
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
