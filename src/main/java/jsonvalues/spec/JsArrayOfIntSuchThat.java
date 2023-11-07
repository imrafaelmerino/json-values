package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfIntSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfInt arrayOfIntSpec;

    JsArrayOfIntSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                         final boolean nullable
                        ) {
        super(nullable);
        this.arrayOfIntSpec = new JsArrayOfInt(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfIntSuchThat(predicate,
                                        true
        );
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfIntSuchThat(predicate,
                                                       nullable
                                                      );
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = arrayOfIntSpec.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());
    }
}
