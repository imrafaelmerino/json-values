package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsArrayOfTestedStrSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    final Function<String, Optional<JsError>> predicate;

    JsArrayOfTestedStrSpec(final Function<String, Optional<JsError>> predicate,
                           final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedStrSpec(final Function<String, Optional<JsError>> predicate,
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
        return new JsArrayOfTestedStrSpec(predicate,
                                          true,
                                          min,
                                          max
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStrEachSuchThat(predicate,
                                                               nullable,
                                                               min,
                                                               max
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isStr() ?
                                                        predicate.apply(v.toJsStr().value) :
                                                        Optional.of(new JsError(v,
                                                                                STRING_EXPECTED
                                                                    )
                                                        ),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
