package jsonvalues;


import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjKeys {

    private OpMapObjKeys() {
    }


    static JsObj map(JsObj json,
                     BiFunction<? super JsPath, ? super JsValue, String> fn,
                     JsPath startingPath
    ) {
        JsObj result = JsObj.empty();

        for (JsObjPair next : json) {
            JsPath headPath = startingPath.key(next.key());
            String keyMapped = fn.apply(headPath,
                                        next.value()
            );
            if (next.value().isObj()) {
                result = result.set(keyMapped,
                                    map(next.value().toJsObj(),
                                        fn,
                                        headPath
                                    )
                );
            } else if (next.value().isArray()) {
                result = result.set(keyMapped,
                                    OpMapArrKeys.map(next.value().toJsArray(),
                                                        fn,
                                                        headPath.index(-1)
                                    )
                );
            } else {
                result = result.set(keyMapped,
                                    next.value()
                );
            }


        }
        return result;

    }

    static JsObj map(JsObj json,
                     Function<? super String, String> fn) {
        JsObj result = JsObj.empty();

        for (JsObjPair next : json) {
            String keyMapped = fn.apply(next.key());
            if (next.value().isObj()) {
                result = result.set(keyMapped,
                                    map(next.value().toJsObj(),
                                        fn
                                    )
                );
            } else if (next.value().isArray()) {
                result = result.set(keyMapped,
                                    OpMapArrKeys.map(next.value().toJsArray(),
                                                        fn
                                    )
                );
            } else {
                result = result.set(keyMapped,
                                    next.value()
                );
            }


        }
        return result;
    }
}
