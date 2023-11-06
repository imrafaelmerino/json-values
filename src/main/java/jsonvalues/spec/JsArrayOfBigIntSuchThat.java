package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfBigIntSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec, AvroSpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfBigInt isArrayOfIntegral;

    JsArrayOfBigIntSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                            final boolean nullable
                           ) {
        super(nullable);
        this.isArrayOfIntegral = new JsArrayOfBigInt(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfBigIntSuchThat(predicate,
                                           true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfIntegralSuchThat(predicate,
                                                            nullable);
    }



    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArrayOfIntegral.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
