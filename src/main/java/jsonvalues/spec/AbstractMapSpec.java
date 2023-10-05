package jsonvalues.spec;


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
                                    new JsError(value,
                                            ERROR_CODE.OBJ_EXPECTED)));
            return errors;
        }


        JsObj obj = value.toJsObj();

        for (var pair : obj) {
            if (predicate.test(pair.value()))
                errors.add(SpecError.of(path.key(pair.key()),
                                        new JsError(pair.value(),
                                                code)));
        }


        return errors;
    }
}
