package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsArrayOfNumberSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfNumberSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfNumberSpec(final boolean nullable,
                        int min,
                        int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfNumberSpec(true,
                                       min,
                                       max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumber(nullable,
                                                      min,
                                                      max);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isNumber() ?
                                                     Optional.empty() :
                                                     Optional.of(Pair.of(v,
                                                                            NUMBER_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
