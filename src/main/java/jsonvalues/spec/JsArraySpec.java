package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;

public interface JsArraySpec extends JsSpec {

    default Set<JsErrorPair> test(final JsArray array) {
        return test(JsPath.empty(),
                    array);
    }





}
