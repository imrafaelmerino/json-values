package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfStrSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec, AvroSpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfStr isArrayOfString;

    JsArrayOfStrSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                         final boolean nullable
                        ) {
        super(nullable);
        this.isArrayOfString = new JsArrayOfStr(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfStrSuchThat(predicate,
                                        true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStrSuchThat(predicate,
                                                           nullable
        );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        Optional<JsError> result = isArrayOfString.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }

    @Override
    public JsValue toAvroSchema() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("string"));

        return nullable ? JsArray.of(JsStr.of("null"), schema) : schema;
    }
}
