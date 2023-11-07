package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;


final class JsArrayOfValue extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {

    JsArrayOfValue(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfValue(final boolean nullable,
                   int min,
                   int max
                  ) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfValue(true,
                                  min,
                                  max);
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfValue(nullable,
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
