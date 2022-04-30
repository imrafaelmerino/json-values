package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrObjs {
    private OpMapArrObjs(){}

    static JsArray map(JsArray json,
                       final BiFunction<? super Integer, ? super JsObj, JsValue> fn
                      ) {

        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isObj()) {
                json = json.set(i,
                                fn.apply(i,
                                         value.toJsObj()
                                        )
                               );
            }
        }
        return json;
    }

    static JsArray mapAll(JsArray json,
                          final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                          final JsPath startingPath) {

        for (int i = json.size() - 1; i >= 0; i--) {
            final JsPath headPath = startingPath.index(i);

            JsValue value = json.get(i);
            if (value.isObj()) {

                JsValue mapped = fn.apply(headPath,
                                          value.toJsObj()
                                         );
                json = json.set(i,
                                mapped.isObj() ?
                                OpMapObjObjs.mapAll(mapped.toJsObj(),
                                                    fn,
                                                    headPath
                                                   ) :
                                value
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
        }
        return json;

    }

    static JsArray map(JsArray json,
                       final Function<? super JsObj, JsValue> fn) {

        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isObj()) {

                json = json.set(i,
                                fn.apply(value.toJsObj())
                               );
            }
        }
        return json;
    }

    static JsArray mapAll(JsArray json,
                          final Function<? super JsObj, JsValue> fn) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isObj()) {

                JsValue mapped = fn.apply(
                        value.toJsObj()
                                         );
                json = json.set(i,
                                mapped.isObj() ?
                                OpMapObjObjs.mapAll(mapped.toJsObj(),
                                                    fn
                                                   ) :
                                value
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                mapAll(value.toJsArray(),
                                       fn
                                      )
                               );
            }
        }
        return json;
    }
}
