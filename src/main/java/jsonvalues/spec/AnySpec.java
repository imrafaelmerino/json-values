package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

class AnySpec implements JsValuePredicate {

    @Override
    public JsSpec nullable() {
        return this;
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofValue();
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return value.isNothing() ?
               Optional.of(Pair.of(value,
                                      ERROR_CODE.REQUIRED)) :
               Optional.empty();
    }
}
