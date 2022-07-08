package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsArrayOfTestedStrSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    final Function<String, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsArrayOfTestedStrSpec(final Function<String, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                           final boolean nullable) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedStrSpec(final Function<String, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
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
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isStr() ?
                                                        predicate.apply(v.toJsStr().value) :
                                                        Optional.of(Pair.of(v,
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
