package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

final class JsArrayOfTestedDouble extends AbstractSizableArr implements JsValuePredicate, JsArraySpec, AvroSpec {
    private final DoubleFunction<Optional<JsError>> predicate;

    JsArrayOfTestedDouble(final DoubleFunction<Optional<JsError>> predicate,
                          final boolean nullable
                         ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedDouble(final DoubleFunction<Optional<JsError>> predicate,
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
        return new JsArrayOfTestedDouble(predicate,
                                         true,
                                         min,
                                         max);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDoubleEachSuchThat(predicate,
                                                                  nullable,
                                                                  min,
                                                                  max
                                                                 );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isDouble() ?
                                                                predicate.apply(v.toJsDouble().value) :
                                                                Optional.of(new JsError(v,
                                                                                        DOUBLE_EXPECTED
                                                                            )
                                                                           ),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }


}
