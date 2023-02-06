package jsonvalues.spec;

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
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArray(nullable,
                                   min,
                                   max)
                        .apply(value);
    }

}
