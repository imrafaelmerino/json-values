package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsArrayOfDecimalSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {

    JsArrayOfDecimalSpec(final boolean required,
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
        return new JsArrayOfDecimalSpec(required,
                                        true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfDecimalSpec(false,
                                        nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimal(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> {
                                                    if (v.isDecimal()) return Optional.empty();
                                                    else return Optional.of(new Error(v,
                                                                                      DECIMAL_EXPECTED));
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
