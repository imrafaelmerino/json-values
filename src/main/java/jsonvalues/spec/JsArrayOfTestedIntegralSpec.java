package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsArrayOfTestedIntegralSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<BigInteger, Optional<Error>> predicate;

    JsArrayOfTestedIntegralSpec(final Function<BigInteger, Optional<Error>> predicate,
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
        return new JsArrayOfTestedIntegralSpec(predicate,
                                               required,
                                               true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedIntegralSpec(predicate,
                                               false,
                                               nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntegralEachSuchThat(predicate,
                                                                    nullable
                                                                   );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                {
                                                    if (v.isIntegral()) return predicate.apply(v.toJsBigInt().value);
                                                    else return Optional.of(new Error(v,
                                                                                      INTEGRAL_EXPECTED
                                                                            )
                                                                           );
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
