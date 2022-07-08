package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfStrSpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfStrSpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfStrSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfString(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isStr(),ERROR_CODE.STRING_EXPECTED);
    }



}
