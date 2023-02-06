package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfDecSpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfDecSpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfDecSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfDecimal(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isDecimal(),ERROR_CODE.DECIMAL_EXPECTED);
    }



}
