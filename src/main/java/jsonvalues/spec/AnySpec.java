package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

final class AnySpec implements JsOneErrorSpec {

    @Override
    public JsSpec nullable() {
        return this;
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofValue();
    }


    @Override
    public boolean isNullable() {
        return true;
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return value.isNothing() ?
                Optional.of(new JsError(value,
                                        ERROR_CODE.REQUIRED)) :
                Optional.empty();
    }
}
