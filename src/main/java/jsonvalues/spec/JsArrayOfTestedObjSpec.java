package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

class JsArrayOfTestedObjSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    final Function<JsObj, Optional<JsError>> predicate;

    JsArrayOfTestedObjSpec(final Function<JsObj, Optional<JsError>> predicate,
                           final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedObjSpec(predicate,
                                          true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfObjEachSuchThat(predicate,
                                                               nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isObj() ?
                                                        predicate.apply(v.toJsObj()) :
                                                        Optional.of(new JsError(v,
                                                                                OBJ_EXPECTED
                                                                    )
                                                        ),
                                                nullable
                        )
                        .apply(value);
    }
}
