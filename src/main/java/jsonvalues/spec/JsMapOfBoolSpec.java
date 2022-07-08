package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfBoolSpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfBoolSpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBoolSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfBool(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isBool(),ERROR_CODE.BOOLEAN_EXPECTED);
    }



}
