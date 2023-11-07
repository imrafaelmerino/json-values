package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


final class JsArrayOfBoolSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfBool arrayOfBoolSpec;

    JsArrayOfBoolSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                          final boolean nullable
                         ) {
        super(nullable);
        this.arrayOfBoolSpec = new JsArrayOfBool(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfBoolSuchThat(predicate,
                                         true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfBoolSuchThat(predicate,
                                                        nullable);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = arrayOfBoolSpec.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());

    }
}
