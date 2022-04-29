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

    JsObjSuchThatSpec(final boolean required,
                      final boolean nullable,
                      final Function<JsObj, Optional<JsError>> predicate
    ) {
        super(required,
              nullable
        );
        this.predicate = predicate;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsObjSuchThatSpec(required,
                                     true,
                                     predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsObjSuchThatSpec(false,
                                     nullable,
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
        Optional<JsError> error = jsonvalues.spec.Functions.testElem(JsValue::isObj,
                                                                     OBJ_EXPECTED,
                                                                     required,
                                                                     nullable
                                            )
                                                           .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsObj());
    }
}
