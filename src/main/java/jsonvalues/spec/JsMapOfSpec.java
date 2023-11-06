package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.List;
import java.util.Objects;


final class JsMapOfSpec extends AbstractMap implements JsSpec, AvroSpec {

    private final JsSpec valueSpec;

    JsMapOfSpec(JsSpec spec) {
        this(false, spec);
    }

    JsMapOfSpec(boolean nullable, JsSpec spec) {
        super(nullable);
        this.valueSpec = Objects.requireNonNull(spec);
    }

    public JsSpec getValueSpec() {
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
    public List<SpecError> test(JsPath path,
                                JsValue value
                               ) {
        return test(path,
                    value,
                    valueSpec);
    }


}
