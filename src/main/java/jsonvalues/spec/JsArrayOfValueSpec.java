package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;


class JsArrayOfValueSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {

    JsArrayOfValueSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfValueSpec(final boolean nullable,
                       int min,
                       int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfValueSpec(true,
                                      min,
                                      max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValue(nullable,
                                                     min,
                                                     max);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArray(nullable,
                                   min,
                                   max)
                        .apply(value);
    }

}
