package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfObjSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfObj arrayOfObjSpec;

    JsArrayOfObjSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                         final boolean nullable
                        ) {
        super(nullable);
        this.arrayOfObjSpec = new JsArrayOfObj(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfObjSuchThat(predicate,
                                        true);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfObjSuchThat(predicate,
                                                       nullable);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        Optional<JsError> result = arrayOfObjSpec.testValue(value);
        return result.isPresent() || value.isNull() ?
                result :
                predicate.apply(value.toJsArray());
    }
}
