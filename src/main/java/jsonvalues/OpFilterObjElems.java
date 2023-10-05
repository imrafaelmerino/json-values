package jsonvalues;


import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjElems {

    private OpFilterObjElems() {}

    static JsObj filter(JsObj json,
                        JsPath startingPath,
                        BiPredicate<? super JsPath, ? super JsPrimitive> predicate
    ) {
        for (var next : json) {
            JsPath headPath = startingPath.key(next.key());

            JsValue headElem = next.value();

            if (headElem.isObj()) {
                json = json.set(next.key(),
                                filter(headElem.toJsObj(),
                                       headPath,
                                       predicate
                                )
                );
            } else if (headElem.isArray()) {
                json = json.set(next.key(),
                                OpFilterArrElems
                                        .filter(headElem.toJsArray(),
                                                headPath,
                                                predicate
                                        )
                );
            } else if (predicate.negate()
                                .test(headPath,
                                      headElem.toJsPrimitive()
                                )) {

                json = json.delete(next.key());
            }

        }

        return json;

    }


    static JsObj filter(JsObj json,
                        Predicate<? super JsPrimitive> predicate) {
        for (var next : json) {

            JsValue headElem = next.value();

            if (headElem.isObj()) {
                json = json.set(next.key(),
                                filter(headElem.toJsObj(),
                                       predicate
                                )
                );
            } else if (headElem.isArray()) {
                json = json.set(next.key(),
                                OpFilterArrElems
                                        .filter(headElem.toJsArray(),
                                                predicate
                                        )
                );
            } else if (predicate.negate()
                                .test(headElem.toJsPrimitive()
                                )) {

                json = json.delete(next.key());
            }

        }

        return json;
    }


}
