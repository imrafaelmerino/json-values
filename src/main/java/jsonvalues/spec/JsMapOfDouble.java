package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.List;


final class JsMapOfDouble extends AbstractMap implements JsSpec, AvroSpec {
    JsMapOfDouble(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfDouble(true);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofMapOfDouble(nullable);
    }

    @Override
    public List<SpecError> test(JsPath path,
                                JsValue value
                               ) {
        return test(path,
                    value,
                    it -> !it.isDouble(),
                    ERROR_CODE.DOUBLE_EXPECTED);
    }

}
