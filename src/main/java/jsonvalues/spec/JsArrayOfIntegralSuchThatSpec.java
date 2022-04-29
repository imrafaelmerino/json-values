package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfIntegralSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfIntegralSpec isArrayOfIntegral;

    JsArrayOfIntegralSuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                                  final boolean required,
                                  final boolean nullable
                                 ) {
        super(required,
              nullable
             );
        this.isArrayOfIntegral = new JsArrayOfIntegralSpec(required,
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
        return new JsArrayOfIntegralSuchThatSpec(predicate,
                                                 required,
                                                 true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfIntegralSuchThatSpec(predicate,
                                                 false,
                                                 nullable
        );

    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntegralSuchThat(predicate,
                                                                nullable
                                                               );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> result = isArrayOfIntegral.test(value);
        if (result.isPresent() || value.isNull()) return result;
        return predicate.apply(value.toJsArray());
    }
}
