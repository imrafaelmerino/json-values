package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


final class JsArraySuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec {

    final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfValue isArray;

    JsArraySuchThat(final Function<JsArray, Optional<JsError>> predicate,
                    final boolean nullable
                   ) {
        super(nullable);
        this.isArray = new JsArrayOfValue(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArraySuchThat(predicate,
                                   true
        );
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfValueSuchThat(predicate,
                                                         nullable
                                                        );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArray.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());
    }
}
