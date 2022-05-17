package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

class JsObjSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {

    final Function<JsObj, Optional<JsError>> predicate;

    JsObjSuchThatSpec(final boolean nullable,
                      final Function<JsObj, Optional<JsError>> predicate
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsObjSuchThatSpec(true,
                                     predicate
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofObjSuchThat(predicate,
                                                    nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        Optional<JsError> error = Functions.testElem(JsValue::isObj,
                                                     OBJ_EXPECTED,
                                                     nullable
                                           )
                                           .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsObj());
    }
}
