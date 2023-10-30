package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfIntSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfInt isArrayOfInt;

    JsArrayOfIntSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                         final boolean nullable
                        ) {
        super(nullable);
        this.isArrayOfInt = new JsArrayOfInt(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfIntSuchThat(predicate,
                                        true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntSuchThat(predicate,
                                                           nullable
        );
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("int"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArrayOfInt.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
