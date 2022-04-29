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
                           final boolean required,
                           final boolean nullable
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
        return new JsArrayOfTestedObjSpec(predicate,
                                          required,
                                          true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfTestedObjSpec(predicate,
                                          false,
                                          nullable
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
                                                {
                                                    if (v.isObj()) return predicate.apply(v.toJsObj());
                                                    else return Optional.of(new JsError(v,
                                                                                        OBJ_EXPECTED
                                                                            )
                                                                           );
                                                },
                                                required,
                                                nullable
                                               )
                        .apply(value);
    }
}
