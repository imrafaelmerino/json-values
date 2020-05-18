package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsPath;

import java.util.Set;

public interface JsArraySpec extends JsSpec {

    default Set<JsErrorPair> test(final JsArray value) {
        return test(JsPath.empty(),
                    value);
    }
}
