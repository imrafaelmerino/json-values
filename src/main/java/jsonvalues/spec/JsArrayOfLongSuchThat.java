package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfLongSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfLong isArrayOfLong;

    JsArrayOfLongSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                          final boolean nullable
                         ) {
        super(nullable);
        this.isArrayOfLong = new JsArrayOfLong(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfLongSuchThat(predicate,
                                         true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLongSuchThat(predicate,
                                                            nullable
        );
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("long"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        Optional<JsError> result = isArrayOfLong.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
