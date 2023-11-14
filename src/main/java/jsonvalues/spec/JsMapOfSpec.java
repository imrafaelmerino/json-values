package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Optional;


final class JsMapOfSpec extends AbstractMap implements JsOneErrorSpec, AvroSpec {

    private final JsSpec valueSpec;

    JsMapOfSpec(JsSpec spec) {
        this(false, spec);
    }

    JsMapOfSpec(boolean nullable, JsSpec spec) {
        super(nullable);
        this.valueSpec = Objects.requireNonNull(spec);
    }

    JsSpec getValueSpec() {
        return valueSpec;
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfSpec(true, valueSpec);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfSpec(valueSpec.parser(),
                                              nullable);
    }


    @Override
    public Optional<JsError> testValue(JsValue value) {
        return test(value,
                    valueSpec);
    }


}
