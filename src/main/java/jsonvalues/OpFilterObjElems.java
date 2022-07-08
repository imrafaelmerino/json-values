package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjElems {

    private OpFilterObjElems() {
    }

    static JsObj filter(JsObj json,
                        JsPath startingPath,
                        BiPredicate<? super JsPath, ? super JsPrimitive> predicate
    ) {
        for (Tuple2<String, JsValue> next : json) {
            JsPath headPath = startingPath.key(next._1);

            JsValue headElem = next._2;

            if (headElem.isObj()) {
                json = json.set(next._1,
                                filter(headElem.toJsObj(),
                                       headPath,
                                       predicate
                                )
                );
            } else if (headElem.isArray()) {
                json = json.set(next._1,
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

                json = json.delete(next._1);
            }

        }

        return json;

    }


    static JsObj filter(JsObj json,
                        Predicate<? super JsPrimitive> predicate) {
        for (Tuple2<String, JsValue> next : json) {

            JsValue headElem = next._2;

            if (headElem.isObj()) {
                json = json.set(next._1,
                                filter(headElem.toJsObj(),
                                       predicate
                                )
                );
            } else if (headElem.isArray()) {
                json = json.set(next._1,
                                OpFilterArrElems
                                        .filter(headElem.toJsArray(),
                                                predicate
                                        )
                );
            } else if (predicate.negate()
                                .test(headElem.toJsPrimitive()
                                )) {

                json = json.delete(next._1);
            }

        }

        return json;
    }


}
