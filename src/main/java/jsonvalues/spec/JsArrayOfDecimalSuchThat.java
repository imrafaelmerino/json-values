package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfDecimalSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec, AvroSpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfDecimal isArrayOfDecimal;

    JsArrayOfDecimalSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                             final boolean nullable
                            ) {
        super(nullable);
        this.isArrayOfDecimal = new JsArrayOfDecimal(nullable);
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
        final Optional<JsError> result = isArrayOfDecimal.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
