package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjKeys {

    static JsObj map(JsObj json,
                     final BiFunction<? super String, ? super JsValue, String> fn
                    ) {
        JsObj result = JsObj.empty();
        for (final Tuple2<String, JsValue> next : json) {
            final String keyMapped = fn.apply(next._1,
                                              next._2
                                             );
            result = result.set(keyMapped,
                                next._2
                               );

        }
        return result;
    }

    static JsObj map(JsObj json,
                     final Function<? super String, String> fn) {
        JsObj result = JsObj.empty();
        for (final Tuple2<String, JsValue> next : json) {
            final String keyMapped = fn.apply(next._1);
            result = result.set(keyMapped,
                                next._2
                               );

        }
        return result;
    }

    static JsObj mapAll(JsObj json,
                        final BiFunction<? super JsPath, ? super JsValue, String> fn,
                        final JsPath startingPath
                       ) {
        JsObj result = JsObj.empty();

        for (final Tuple2<String, JsValue> next : json) {
            final JsPath headPath = startingPath.key(next._1);
            final String keyMapped = fn.apply(headPath,
                                              next._2
                                             );
            if (next._2.isObj()) {
                result = result.set(keyMapped,
                                    mapAll(next._2.toJsObj(),
                                           fn,
                                           headPath
                                          )
                                   );
            }
            else if (next._2.isArray()) {
                result = result.set(keyMapped,
                                    OpMapArrKeys.mapAll(next._2.toJsArray(),
                                                        fn,
                                                        headPath.index(-1)
                                                       )
                                   );
            }
            else {
                result = result.set(keyMapped,
                                    next._2
                                   );
            }


        }
        return result;

    }

    static JsObj mapAll(JsObj json,
                        final Function<? super String, String> fn) {
        JsObj result = JsObj.empty();

        for (final Tuple2<String, JsValue> next : json) {
            final String keyMapped = fn.apply(next._1);
            if (next._2.isObj()) {
                result = result.set(keyMapped,
                                    mapAll(next._2.toJsObj(),
                                           fn
                                          )
                                   );
            }
            else if (next._2.isArray()) {
                result = result.set(keyMapped,
                                    OpMapArrKeys.mapAll(next._2.toJsArray(),
                                                        fn
                                                       )
                                   );
            }
            else {
                result = result.set(keyMapped,
                                    next._2
                                   );
            }


        }
        return result;
    }
}
