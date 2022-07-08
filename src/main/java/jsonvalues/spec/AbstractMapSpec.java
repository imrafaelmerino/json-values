package jsonvalues.spec;

import fun.tuple.Pair;
import io.vavr.Tuple2;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

abstract class AbstractMapSpec extends AbstractNullableSpec {
     AbstractMapSpec(boolean nullable) {
        super(nullable);
    }

    protected Set<SpecError> test(JsPath path,
                        JsValue value,
                        Predicate<JsValue> predicate,
                        ERROR_CODE code) {
        Set<SpecError> errors = new HashSet<>();
        if (!value.isObj()) {
            errors.add(SpecError.of(path,
                                    Pair.of(value,
                                            ERROR_CODE.OBJ_EXPECTED)));
            return errors;
        }


        JsObj obj = value.toJsObj();

        for (Tuple2<String, JsValue> pair : obj) {
            if (predicate.test(pair._2))
                errors.add(SpecError.of(path.key(pair._1),
                                        Pair.of(pair._2,
                                                code)));
        }


        return errors;
    }
}
