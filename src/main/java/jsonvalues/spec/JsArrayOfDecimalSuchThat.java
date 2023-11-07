package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfDecimalSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfDecimal arrayOfDecimalSpec;

    JsArrayOfDecimalSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                             final boolean nullable
                            ) {
        super(nullable);
        this.arrayOfDecimalSpec = new JsArrayOfDecimal(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfDecimalSuchThat(predicate,
                                            true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfDecimalSuchThat(predicate,
                                                           nullable);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = arrayOfDecimalSpec.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());
    }
}
