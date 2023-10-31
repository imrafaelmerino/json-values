package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

final class JsArrayOfTestedNumber extends AbstractSizableArr implements JsValuePredicate, JsArraySpec, AvroSpec {
    private final Function<JsNumber, Optional<JsError>> predicate;

    JsArrayOfTestedNumber(final Function<JsNumber, Optional<JsError>> predicate,
                          final boolean nullable
                         ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedNumber(final Function<JsNumber, Optional<JsError>> predicate,
                          final boolean nullable,
                          int min,
                          int max
                         ) {
        super(nullable,
              min,
              max);
        this.predicate = predicate;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedNumber(predicate,
                                         true,
                                         min,
                                         max
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumberEachSuchThat(predicate,
                                                                  nullable,
                                                                  min,
                                                                  max
                                                                 );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isNumber() ?
                                                                predicate.apply(v.toJsNumber()) :
                                                                Optional.of(new JsError(v,
                                                                                        NUMBER_EXPECTED
                                                                            )
                                                                           ),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }

    @Override
    public JsValue toAvroSchema() {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsArray.of("int", "long", "double"));

        return nullable ? JsArray.of(JsStr.of("null"), schema) : schema;
    }
}
