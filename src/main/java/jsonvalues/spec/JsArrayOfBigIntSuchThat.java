package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfBigIntSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfBigInt arrayOfIntegralSpec;

    JsArrayOfBigIntSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                            final boolean nullable
                           ) {
        super(nullable);
        this.arrayOfIntegralSpec = new JsArrayOfBigInt(nullable);
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
        final Optional<JsError> result = arrayOfIntegralSpec.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());
    }
}
