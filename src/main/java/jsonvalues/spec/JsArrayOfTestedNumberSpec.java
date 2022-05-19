package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsArrayOfTestedNumberSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsNumber, Optional<JsError>> predicate;

    JsArrayOfTestedNumberSpec(final Function<JsNumber, Optional<JsError>> predicate,
                              final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedNumberSpec(final Function<JsNumber, Optional<JsError>> predicate,
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
        return new JsArrayOfTestedNumberSpec(predicate,
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
    public Optional<JsError> test(final JsValue value) {
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
}
