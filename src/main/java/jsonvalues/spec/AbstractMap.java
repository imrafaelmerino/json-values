package jsonvalues.spec;


import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

abstract class AbstractMap extends AbstractNullable {

    AbstractMap(boolean nullable) {
        super(nullable);
    }


    protected List<SpecError> test(JsPath path,
                                   JsValue value,
                                   Predicate<JsValue> predicate,
                                   ERROR_CODE code
                                  ) {
        List<SpecError> errors = new ArrayList<>();
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

    protected List<SpecError> test(JsPath path,
                                   JsValue value,
                                   JsSpec spec
                                  ) {
        List<SpecError> errors = new ArrayList<>();
        if (!value.isObj()) {
            errors.add(SpecError.of(path,
                                    new JsError(value,
                                                ERROR_CODE.OBJ_EXPECTED)));
            return errors;
        }


        JsObj obj = value.toJsObj();

        for (var pair : obj) {
            List<SpecError> xs = spec.test(pair.value());
            if (!xs.isEmpty())
                errors.addAll(xs);
        }


        return errors;
    }

}
