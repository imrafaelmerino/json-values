package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfBigIntSpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfBigIntSpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBigIntSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfBigInt(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isIntegral(),ERROR_CODE.INTEGRAL_EXPECTED);
    }



}
