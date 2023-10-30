package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfNumberSuchThat extends AbstractNullable implements JsValuePredicate, JsArraySpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfNumber isArrayOfNumber;

    JsArrayOfNumberSuchThat(final Function<JsArray, Optional<JsError>> predicate,
                            final boolean nullable
                           ) {
        super(nullable);
        this.isArrayOfNumber = new JsArrayOfNumber(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfNumberSuchThat(predicate,
                                           true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumberSuchThat(predicate,
                                                              nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        Optional<JsError> result = isArrayOfNumber.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsArray.of("int","long","double"));

        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }
}
