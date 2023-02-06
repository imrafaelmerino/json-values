package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfInstantSpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfInstantSpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfInstantSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfInstant(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
        return test(path,
                    value,
                    it -> !it.isInstant(),
                    ERROR_CODE.INSTANT_EXPECTED);
    }


}
