package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;


class JsMapOfBinarySpec extends AbstractMapSpec implements JsSpec {
    protected JsMapOfBinarySpec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBinarySpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfBinary(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isBinary(),ERROR_CODE.BINARY_EXPECTED);
    }



}
