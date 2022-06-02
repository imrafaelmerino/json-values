package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.REQUIRED;

class AnySuchThatSpec implements JsValuePredicate {

    private final Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    AnySuchThatSpec(final Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> predicate) {
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return this;
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofValueSuchThat(predicate);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return value.isNothing() ?
               Optional.of(Pair.of(value,
                                   REQUIRED)) :
               predicate.apply(value);

    }
}
