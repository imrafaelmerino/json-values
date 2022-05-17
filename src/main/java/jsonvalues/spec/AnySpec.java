package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
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
    public Optional<JsError> test(final JsValue value) {

        if (value.isNothing())
            return Optional.of(new JsError(value,
                                           ERROR_CODE.REQUIRED));
        return Optional.empty();

    }
}
