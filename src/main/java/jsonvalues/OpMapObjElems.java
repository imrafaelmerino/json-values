package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjElems {

    private OpMapObjElems() {
    }


    static JsObj map(JsObj json,
                     BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                     JsPath startingPath
    ) {
        for (Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isObj()) {
                json = json.set(tuple._1,
                                map(tuple._2.toJsObj(),
                                    fn,
                                    startingPath.key(tuple._1)
                                )
                );
            } else if (tuple._2.isArray()) {
                json = json.set(tuple._1,
                                OpMapArrElems.map(tuple._2.toJsArray(),
                                                  fn,
                                                  startingPath.index(-1)
                                )
                );
            } else {
                JsPath headPath = startingPath.key(tuple._1);

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

    static JsObj map(JsObj json,
                     Function<? super JsPrimitive, ? extends JsValue> fn) {
        if (json.isEmpty()) return json;
        for (Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isObj()) {
                json = json.set(tuple._1,
                                map(tuple._2.toJsObj(),
                                    fn
                                )
                );
            } else if (tuple._2.isArray()) {
                json = json.set(tuple._1,
                                OpMapArrElems.map(tuple._2.toJsArray(),
                                                  fn
                                )
                );
            } else {

                JsValue headMapped = fn.apply(tuple._2.toJsPrimitive());
                json = json.set(tuple._1,
                                headMapped
                );
            }
        }

        return json;
    }
}
