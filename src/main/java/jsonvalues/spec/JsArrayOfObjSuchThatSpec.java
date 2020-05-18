package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfObjSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<Error>> predicate;
    private JsArrayOfObjSpec isArrayOfObj;

    JsArrayOfObjSuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                             final boolean required,
                             final boolean nullable
                            ) {
        super(required,
              nullable
             );
        this.isArrayOfObj = new JsArrayOfObjSpec(required,
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
        return new JsArrayOfObjSuchThatSpec(predicate,
                                            required,
                                            true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfObjSuchThatSpec(predicate,
                                            false,
                                            nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfObjSuchThat(predicate,
                                                           nullable
                                                          );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> result = isArrayOfObj.test(value);
        if (result.isPresent() || value.isNull()) return result;
        return predicate.apply(value.toJsArray());
    }
}
