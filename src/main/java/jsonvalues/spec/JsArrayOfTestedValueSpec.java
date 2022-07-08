package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfTestedValueSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsArrayOfTestedValueSpec(final Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                             final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedValueSpec(final Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
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
        return new JsArrayOfTestedValueSpec(predicate,
                                            true,
                                            min,
                                            max
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValueEachSuchThat(predicate,
                                                                 nullable,
                                                                 min,
                                                                 max);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(predicate,
                                                nullable,
                                                min,
                                                max)
                        .apply(value);
    }
}
