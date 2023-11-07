package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfStrSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfStr arrayOfStringSpec;

    JsArrayOfStrSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                         final boolean nullable
                        ) {
        super(nullable);
        this.arrayOfStringSpec = new JsArrayOfStr(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfStrSuchThat(predicate,
                                        true
        );
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfStrSuchThat(predicate,
                                                       nullable
                                                      );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        Optional<JsError> result = arrayOfStringSpec.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());
    }

}
