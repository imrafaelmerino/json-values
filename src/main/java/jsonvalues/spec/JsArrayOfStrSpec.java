package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsArrayOfStrSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfStrSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfStrSpec(final boolean nullable,
                     int min,
                     int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfStrSpec(true,
                                    min,
                                    max);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStr(nullable,
                                                   min,
                                                   max);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isStr() ?
                                                     Optional.empty() :
                                                     Optional.of(Pair.of(v,
                                                                         STRING_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
