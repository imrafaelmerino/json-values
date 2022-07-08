package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterArrElems {

    private OpFilterArrElems() {
    }

    static JsArray filter(JsArray json,
                          Predicate<? super JsPrimitive> predicate) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                OpFilterObjElems.filter(value.toJsObj(),
                                                        predicate
                                )
                );
            } else if (value.isArray()) {
                json = json.set(i,
                                filter(value.toJsArray(),
                                       predicate
                                )
                );
            } else if (predicate.negate()
                                .test(
                                        value.toJsPrimitive()
                                )) {
                json = json.delete(i);
            }
        }

        return json;
    }


    static JsArray filter(JsArray json,
                          JsPath startingPath,
                          BiPredicate<? super JsPath, ? super JsPrimitive> predicate
    ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsPath headPath = startingPath.index(i);
            JsValue value = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                OpFilterObjElems.filter(value.toJsObj(),
                                                        headPath,
                                                        predicate
                                )
                );
            } else if (value.isArray()) {
                json = json.set(i,
                                filter(value.toJsArray(),
                                       headPath,
                                       predicate
                                )
                );
            } else if (predicate.negate()
                                .test(headPath,
                                      value.toJsPrimitive()
                                )) {
                json = json.delete(i);
            }
        }

        return json;
    }

}
