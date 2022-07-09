package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfArraySpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfArraySpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfArraySpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfArray(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isArray(),ERROR_CODE.ARRAY_EXPECTED);
    }



}
