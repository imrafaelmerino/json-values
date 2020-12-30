package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjObjs {

    static JsObj map(JsObj json,
                     final BiFunction<? super String, ? super JsObj, JsValue> fn
                    ) {
        for (final Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {
                json = json.set(next._1,
                                fn.apply(next._1,
                                         next._2.toJsObj()
                                        )
                               );
            }
        }
        return json;
    }

    static JsObj mapAll(JsObj json,
                 final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                 final JsPath startingPath
                ) {
        for (final Tuple2<String, JsValue> next : json) {
            final JsPath headPath = startingPath.key(next._1);

            if (next._2.isObj()) {
                JsValue mapped = fn.apply(headPath,
                                          next._2.toJsObj()
                                         );
                json = json.set(next._1,
                                mapped.isObj() ?
                                mapAll(mapped.toJsObj(),
                                       fn,
                                       headPath
                                      )
                                               : mapped
                               );
            }
            else if (next._2.isArray()) {
                json = json.set(next._1,
                                OpMapArrObjs.mapAll(next._2.toJsArray(),
                                                    fn,
                                                    headPath
                                                   )
                               );
            }

        }
        return json;

    }

    static JsObj map(JsObj json,
              final Function<? super JsObj, JsValue> fn) {
        for (final Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {

                json = json.set(next._1,
                                fn.apply(
                                        next._2.toJsObj()
                                        )
                               );
            }
        }
        return json;
    }

    static JsObj mapAll(JsObj json,
                 final Function<? super JsObj, JsValue> fn) {
        for (final Tuple2<String, JsValue> next : json) {

            if (next._2.isObj()) {
                JsValue mapped = fn.apply(
                        next._2.toJsObj()
                                         );
                json = json.set(next._1,
                                mapped.isObj() ?
                                mapAll(mapped.toJsObj(),
                                       fn
                                      )
                                               : mapped
                               );
            }
            else if (next._2.isArray()) {
                json = json.set(next._1,
                                OpMapArrObjs.mapAll(next._2.toJsArray(),
                                                    fn
                                                   )
                               );
            }

        }
        return json;

    }
}
