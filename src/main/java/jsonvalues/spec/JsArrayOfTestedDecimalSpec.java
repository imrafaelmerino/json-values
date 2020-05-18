package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsArrayOfTestedDecimalSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    final Function<BigDecimal, Optional<Error>> predicate;

    JsArrayOfTestedDecimalSpec(final Function<BigDecimal, Optional<Error>> predicate,
                               final boolean required,
                               final boolean nullable
                              ) {
        super(required,
              nullable
             );
        this.predicate = predicate;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedDecimalSpec(predicate,
                                              required,
                                              true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedDecimalSpec(predicate,
                                              false,
                                              nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimalEachSuchThat(predicate,
                                                                   nullable
                                                                  );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                {
                                                    if (v.isDouble() || v.isBigDec())
                                                        return predicate.apply(v.toJsBigDec().value);
                                                    else return Optional.of(new Error(v,
                                                                                      DECIMAL_EXPECTED
                                                                            )
                                                                           );
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
