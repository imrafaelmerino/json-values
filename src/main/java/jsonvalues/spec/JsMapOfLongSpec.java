package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import io.vavr.Tuple2;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;

class JsMapOfLongSpec extends AbstractNullableSpec implements JsSpec {
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
        Set<SpecError> errors = new HashSet<>();
        if (!value.isObj()) {
            errors.add(SpecError.of(path,
                                    Pair.of(value,
                                            ERROR_CODE.OBJ_EXPECTED)));
            return errors;
        }


        JsObj obj = value.toJsObj();

        for (Tuple2<String, JsValue> pair : obj) {
            if (!pair._2.isLong() || !pair._2.isInt())
                errors.add(SpecError.of(path,
                                        Pair.of(value,
                                                ERROR_CODE.LONG_EXPECTED)));
        }


        return errors;
    }
}
