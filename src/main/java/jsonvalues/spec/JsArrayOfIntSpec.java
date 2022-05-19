package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsArrayOfIntSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {


    JsArrayOfIntSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfIntSpec(final boolean nullable,
                     int min,
                     int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfIntSpec(true,
                                    min,
                                    max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfInt(nullable,
                                                   min,
                                                   max);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isInt() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                             INT_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);

    }
}
