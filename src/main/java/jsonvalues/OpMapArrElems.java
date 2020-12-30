package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrElems {

    static JsArray map(JsArray json,
                       final BiFunction<? super Integer, ? super JsPrimitive, ? extends JsValue> fn
                      ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isPrimitive()) {
                JsValue headMapped = fn.apply(i,
                                              value.toJsPrimitive()
                                             );
                json = json.set(i,
                                headMapped
                               );
            }

        }

        return json;
    }

    static JsArray mapAll(JsArray json,
                          final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                          final JsPath startingPath
                         ) {
        for (int i = json.size() - 1; i >= 0; i--) {
            final JsPath headPath = startingPath.index(i);
            JsValue      value    = json.get(i);
            if (value.isObj()) {
                json = json.set(i,
                                OpMapObjElems.mapAll(value.toJsObj(),
                                                     fn,
                                                     headPath
                                                    )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                mapAll(value.toJsArray(),
                                       fn,
                                       headPath
                                      )
                               );
            }
            else {

                JsValue headMapped = fn.apply(headPath,
                                              value.toJsPrimitive()
                                             );
                json = json.set(i,
                                headMapped
                               );
            }

        }

        return json;

    }

    static JsArray mapAll(JsArray json,
                          final Function<? super JsPrimitive, ? extends JsValue> fn) {
        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue value = json.get(i);
            if (value.isObj()) {
                json = json.set(i,
                                OpMapObjElems.mapAll(value.toJsObj(),
                                                     fn
                                                    )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                OpMapArrElems.mapAll(value.toJsArray(),
                                                     fn
                                                    )
                               );
            }
            else {

                JsValue headMapped = fn.apply(value.toJsPrimitive());
                json = json.set(i,
                                headMapped
                               );
            }

        }

        return json;
    }

    static JsArray map(JsArray json,
                       final Function<? super JsPrimitive, ? extends JsValue> fn) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isPrimitive()) {
                JsValue headMapped = fn.apply(value.toJsPrimitive());
                json = json.set(i,
                                headMapped
                               );
            }

        }

        return json;
    }
}

