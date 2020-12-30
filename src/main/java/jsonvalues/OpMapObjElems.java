package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjElems {

    static JsObj map(JsObj json,
                     final BiFunction<? super String, ? super JsPrimitive, ? extends JsValue> fn
                    ) {
        for (final Tuple2<String, JsValue> tuple : json) {

            if (tuple._2.isPrimitive()) {
                JsValue headMapped = fn.apply(tuple._1,
                                              tuple._2.toJsPrimitive()
                                             );
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;


    }

    static JsObj map(JsObj json,
                     final Function<? super JsPrimitive, ? extends JsValue> fn) {
        for (final Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isPrimitive()) {
                JsValue headMapped = fn.apply(tuple._2.toJsPrimitive());
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;


    }

    static JsObj mapAll(JsObj json,
                        final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                        final JsPath startingPath
                       ) {
        for (final Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isObj()) {
                json = json.set(tuple._1,
                                mapAll(tuple._2.toJsObj(),
                                       fn,
                                       startingPath.key(tuple._1)
                                      )
                               );
            }
            else if (tuple._2.isArray()) {
                json = json.set(tuple._1,
                                OpMapArrElems.mapAll(tuple._2.toJsArray(),
                                                     fn,
                                                     startingPath.key(tuple._1)
                                                    )
                               );
            }
            else {
                final JsPath headPath = startingPath.key(tuple._1);

                JsValue headMapped = fn.apply(headPath,
                                              tuple._2.toJsPrimitive()
                                             );
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;

    }

    static JsObj mapAll(JsObj json,
                        final Function<? super JsPrimitive, ? extends JsValue> fn) {
        if (json.isEmpty()) return json;
        for (final Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isObj()) {
                json = json.set(tuple._1,
                                mapAll(tuple._2.toJsObj(),
                                       fn
                                      )
                               );
            }
            else if (tuple._2.isArray()) {
                json = json.set(tuple._1,
                                OpMapArrElems.mapAll(tuple._2.toJsArray(),
                                                     fn
                                                    )
                               );
            }
            else {

                JsValue headMapped = fn.apply(tuple._2.toJsPrimitive());
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;
    }
}
