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
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimalSuchThat(predicate,
                                                               nullable);
    }

    @Override
    public JsValue toAvroSchema() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("double"));

        return nullable ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArrayOfDecimal.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
