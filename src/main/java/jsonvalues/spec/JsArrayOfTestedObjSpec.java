package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

class JsArrayOfTestedObjSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsArrayOfTestedObjSpec(final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                           final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedObjSpec(final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
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
        return new JsArrayOfTestedObjSpec(predicate,
                                          true,
                                          min,
                                          max
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfObjEachSuchThat(predicate,
                                                               nullable,
                                                               min,
                                                               max
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isObj() ?
                                                        predicate.apply(v.toJsObj()) :
                                                        Optional.of(Pair.of(v,
                                                                               OBJ_EXPECTED
                                                                    )
                                                        ),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
