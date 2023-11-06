package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfTestedValue extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {
    private final Function<JsValue, Optional<JsError>> predicate;

    JsArrayOfTestedValue(final Function<JsValue, Optional<JsError>> predicate,
                         final boolean nullable
                        ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedValue(final Function<JsValue, Optional<JsError>> predicate,
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
        return new JsArrayOfTestedValue(predicate,
                                        true,
                                        min,
                                        max
        );
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfValueEachSuchThat(predicate,
                                                             nullable,
                                                             min,
                                                             max);
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(predicate,
                                                nullable,
                                                min,
                                                max)
                        .apply(value);
    }
}
