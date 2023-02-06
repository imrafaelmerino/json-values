package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsArrayOfTestedIntSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    final IntFunction<Optional<JsError>> predicate;

    JsArrayOfTestedIntSpec(final IntFunction<Optional<JsError>> predicate,
                           final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedIntSpec(final IntFunction<Optional<JsError>> predicate,
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
        return new JsArrayOfTestedIntSpec(predicate,
                                          true,
                                          min,
                                          max
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntEachSuchThat(predicate,
                                                               nullable,
                                                               min,
                                                               max
        );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {

        return Functions.testArrayOfTestedValue(v ->
                                                        v.isInt() ?
                                                        predicate.apply(v.toJsInt().value) :
                                                        Optional.of(new JsError(v,
                                                                               INT_EXPECTED
                                                                    )
                                                        ),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
