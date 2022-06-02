package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class JsArrayOfBoolSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfBoolSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfBoolSpec(final boolean nullable,
                      int min,
                      int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfBoolSpec(true,
                                     min,
                                     max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfBool(nullable,
                                                    min,
                                                    max);
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isBool() ?
                                                     Optional.empty() :
                                                     Optional.of(Pair.of(v,
                                                                            BOOLEAN_EXPECTED)),
                                                nullable,
                                                min,
                                                max)
                        .apply(value);
    }
}
