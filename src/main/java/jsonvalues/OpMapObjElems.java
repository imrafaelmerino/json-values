package jsonvalues;


import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjElems {

    private OpMapObjElems() {
    }


    static JsObj map(JsObj json,
                     BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                     JsPath startingPath
    ) {
        for (var tuple : json) {
            if (tuple.value().isObj()) {
                json = json.set(tuple.key(),
                                map(tuple.value().toJsObj(),
                                    fn,
                                    startingPath.key(tuple.key())
                                )
                );
            } else if (tuple.value().isArray()) {
                json = json.set(tuple.key(),
                                OpMapArrElems.map(tuple.value().toJsArray(),
                                                  fn,
                                                  startingPath.index(-1)
                                )
                );
            } else {
                JsPath headPath = startingPath.key(tuple.key());

                JsValue headMapped = fn.apply(headPath,
                                              tuple.value().toJsPrimitive()
                );
                json = json.set(tuple.key(),
                                headMapped
                );
            }
        }

        return json;

    }

    static JsObj map(JsObj json,
                     Function<? super JsPrimitive, ? extends JsValue> fn) {
        if (json.isEmpty()) return json;
        for (var tuple : json) {
            if (tuple.value().isObj()) {
                json = json.set(tuple.key(),
                                map(tuple.value().toJsObj(),
                                    fn
                                )
                );
            } else if (tuple.value().isArray()) {
                json = json.set(tuple.key(),
                                OpMapArrElems.map(tuple.value().toJsArray(),
                                                  fn
                                )
                );
            } else {

                JsValue headMapped = fn.apply(tuple.value().toJsPrimitive());
                json = json.set(tuple.key(),
                                headMapped
                );
            }
        }

        return json;
    }
}
