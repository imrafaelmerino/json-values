package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

class JsArrayOfObjSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfObjSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfObjSpec(final boolean nullable,
                     int min,
                     int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfObjSpec(true,
                                    min,
                                    max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfObj(nullable,
                                                   min,
                                                   max);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isObj() ?
                                                     Optional.empty() :
                                                     Optional.of(Pair.of(v,
                                                                         OBJ_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
