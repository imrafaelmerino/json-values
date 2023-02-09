package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjObjs {

    private OpMapObjObjs() {
    }


    static JsObj map(JsObj json,
                     BiFunction<? super JsPath, ? super JsObj, ? extends JsValue> fn,
                     JsPath startingPath
    ) {
        for (JsObjPair next : json) {
            JsPath headPath = startingPath.key(next.key());

            if (next.value().isObj()) {
                JsValue mapped = fn.apply(headPath,
                                          next.value().toJsObj()
                );
                json = json.set(next.key(),
                                mapped.isObj() ?
                                map(mapped.toJsObj(),
                                    fn,
                                    headPath
                                )
                                               :
                                mapped
                );
            } else if (next.value().isArray()) {
                json = json.set(next.key(),
                                OpMapArrObjs.map(next.value().toJsArray(),
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
        for (JsObjPair next : json) {

            if (next.value().isObj()) {
                JsValue mapped = fn.apply(
                        next.value().toJsObj()
                );
                json = json.set(next.key(),
                                mapped.isObj() ?
                                map(mapped.toJsObj(),
                                    fn
                                )
                                               :
                                mapped
                );
            } else if (next.value().isArray()) {
                json = json.set(next.key(),
                                OpMapArrObjs.map(next.value().toJsArray(),
                                                 fn
                                )
                );
            }

        }
        return json;

    }
}
