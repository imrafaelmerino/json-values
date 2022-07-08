package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjObjs {

    private OpMapObjObjs() {
    }


    static JsObj map(JsObj json,
                     BiFunction<? super JsPath, ? super JsObj, ? extends JsValue> fn,
                     JsPath startingPath
    ) {
        for (Tuple2<String, JsValue> next : json) {
            JsPath headPath = startingPath.key(next._1);

            if (next._2.isObj()) {
                JsValue mapped = fn.apply(headPath,
                                          next._2.toJsObj()
                );
                json = json.set(next._1,
                                mapped.isObj() ?
                                map(mapped.toJsObj(),
                                    fn,
                                    headPath
                                )
                                               :
                                mapped
                );
            } else if (next._2.isArray()) {
                json = json.set(next._1,
                                OpMapArrObjs.map(next._2.toJsArray(),
                                                 fn,
                                                 headPath.index(-1)
                                )
                );
            }

        }
        return json;

    }


    static JsObj map(JsObj json,
                     final Function<? super JsObj, ? extends JsValue> fn) {
        for (Tuple2<String, JsValue> next : json) {

            if (next._2.isObj()) {
                JsValue mapped = fn.apply(
                        next._2.toJsObj()
                );
                json = json.set(next._1,
                                mapped.isObj() ?
                                map(mapped.toJsObj(),
                                    fn
                                )
                                               :
                                mapped
                );
            } else if (next._2.isArray()) {
                json = json.set(next._1,
                                OpMapArrObjs.map(next._2.toJsArray(),
                                                 fn
                                )
                );
            }

        }
        return json;

    }
}
