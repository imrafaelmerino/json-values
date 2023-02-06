package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;


import java.util.Set;


class JsMapOfLongSpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfLongSpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfLongSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfLong(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isLong() && !it.isInt(),ERROR_CODE.LONG_EXPECTED);
    }



}
