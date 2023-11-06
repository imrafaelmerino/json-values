package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


final class JsArrayOfDoubleSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfDouble arrayOfDoubleSpec;

    JsArrayOfDoubleSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                            final boolean nullable
                           ) {
        super(nullable);
        this.arrayOfDoubleSpec = new JsArrayOfDouble(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfDoubleSuchThat(predicate,
                                           true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfDoubleSuchThat(predicate,
                                                          nullable);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = arrayOfDoubleSpec.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());

    }
}
