package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;


final class JsArrayOfBoolSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfBool isArrayOfBool;

    JsArrayOfBoolSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                          final boolean nullable
                         ) {
        super(nullable);
        this.isArrayOfBool = new JsArrayOfBool(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfBoolSuchThat(predicate,
                                         true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfBoolSuchThat(predicate,
                                                            nullable);
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("boolean"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArrayOfBool.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());

    }
}
