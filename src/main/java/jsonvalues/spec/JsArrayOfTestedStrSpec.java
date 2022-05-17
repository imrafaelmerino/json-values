package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsArrayOfTestedStrSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    final Function<String, Optional<JsError>> predicate;

    JsArrayOfTestedStrSpec(final Function<String, Optional<JsError>> predicate,
                           final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedStrSpec(predicate,
                                          true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStrEachSuchThat(predicate,
                                                               nullable
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
                                                nullable
                        )
                        .apply(value);
    }
}
