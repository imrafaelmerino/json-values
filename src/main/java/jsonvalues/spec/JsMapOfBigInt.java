package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;


final class JsMapOfBigInt extends AbstractMap implements JsSpec,AvroSpec {
    JsMapOfBigInt(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBigInt(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfBigInt(nullable);
    }

    @Override
    public List<SpecError> test(JsPath path,
                                JsValue value
                               ) {
        return test(path, value, it -> !it.isIntegral(), ERROR_CODE.INTEGRAL_EXPECTED);
    }


}
