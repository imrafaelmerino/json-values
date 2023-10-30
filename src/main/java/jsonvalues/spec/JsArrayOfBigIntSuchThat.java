package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfBigIntSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfBigInt isArrayOfIntegral;

    JsArrayOfBigIntSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                            final boolean nullable
                           ) {
        super(nullable);
        this.isArrayOfIntegral = new JsArrayOfBigInt(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfBigIntSuchThat(predicate,
                                           true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntegralSuchThat(predicate,
                                                                nullable);
    }

    @Override
    public JsValue toAvro() {
        JsObj items = JsObj.of("type", JsStr.of("string"),
                               "logicalType", JsStr.of("biginteger"));

        JsObj schema = JsObj.of("type",JsStr.of("array"),
                                "items",items);

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArrayOfIntegral.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
