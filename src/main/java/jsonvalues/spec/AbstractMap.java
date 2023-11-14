package jsonvalues.spec;


import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Predicate;

abstract class AbstractMap extends AbstractNullable {

    AbstractMap(boolean nullable) {
        super(nullable);
    }


    protected Optional<JsError> test(JsValue value,
                                     Predicate<JsValue> isError,
                                     ERROR_CODE code
                                    ) {
        if (value.isNull() && nullable) return Optional.empty();
        if (!value.isObj()) {
            return Optional.of(new JsError(value,
                                           ERROR_CODE.OBJ_EXPECTED));
        }


        var obj = value.toJsObj();

        for (var pair : obj) {
            if (isError.test(pair.value()))
                return Optional.of(new JsError(pair.value(), code));
        }


        return Optional.empty();
    }

    protected Optional<JsError> test(JsValue value,
                                     JsSpec spec
                                    ) {
        if (value.isNull() && nullable) return Optional.empty();

        if (!value.isObj())
            return Optional.of(new JsError(value,
                                           ERROR_CODE.OBJ_EXPECTED));

        var obj = value.toJsObj();

        for (var pair : obj) {
            var xs = spec.test(pair.value());
            if (!xs.isEmpty())
                return Optional.of(xs.get(0).error);
        }


        return Optional.empty();
    }

}
