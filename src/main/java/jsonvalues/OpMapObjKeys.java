package jsonvalues;

import io.vavr.Tuple2;

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

        for (Tuple2<String, JsValue> next : json) {
            JsPath headPath = startingPath.key(next._1);
            String keyMapped = fn.apply(headPath,
                                        next._2
            );
            if (next._2.isObj()) {
                result = result.set(keyMapped,
                                    map(next._2.toJsObj(),
                                        fn,
                                        headPath
                                    )
                );
            } else if (next._2.isArray()) {
                result = result.set(keyMapped,
                                    OpMapArrKeys.map(next._2.toJsArray(),
                                                        fn,
                                                        headPath.index(-1)
                                    )
                );
            } else {
                result = result.set(keyMapped,
                                    next._2
                );
            }


        }
        return result;

    }

    static JsObj map(JsObj json,
                     Function<? super String, String> fn) {
        JsObj result = JsObj.empty();

        for (Tuple2<String, JsValue> next : json) {
            String keyMapped = fn.apply(next._1);
            if (next._2.isObj()) {
                result = result.set(keyMapped,
                                    map(next._2.toJsObj(),
                                        fn
                                    )
                );
            } else if (next._2.isArray()) {
                result = result.set(keyMapped,
                                    OpMapArrKeys.map(next._2.toJsArray(),
                                                        fn
                                    )
                );
            } else {
                result = result.set(keyMapped,
                                    next._2
                );
            }


        }
        return result;
    }
}
