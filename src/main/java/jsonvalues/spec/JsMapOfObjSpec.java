
package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfObjSpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfObjSpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfObjSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfObj(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isObj(),ERROR_CODE.OBJ_EXPECTED);
    }



}
