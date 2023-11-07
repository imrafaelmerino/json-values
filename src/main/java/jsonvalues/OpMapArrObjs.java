package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrObjs {
    private OpMapArrObjs() {
    }


    static JsArray map(JsArray json,
                       BiFunction<? super JsPath, ? super JsObj, ? extends JsValue> fn,
                       JsPath startingPath
                      ) {
        JsPath headPath = startingPath;

        for (int i = 0; i < json.size(); i++) {
            headPath = headPath.inc();

            JsValue value = json.get(i);
            if (value.isObj()) {

                JsValue mapped = fn.apply(headPath,
                                          value.toJsObj()
                                         );
                json = json.set(i,
                                mapped.isObj() ?
                                        OpMapObjObjs.map(mapped.toJsObj(),
                                                         fn,
                                                         headPath
                                                        ) :
                                        value
                               );
            } else if (value.isArray()) {
                json = json.set(i,
                                map(value.toJsArray(),
                                    fn,
                                    headPath.index(-1)
                                   )
                               );
            }
        }
        return json;

    }


    static JsArray map(JsArray json,
                       Function<? super JsObj, ? extends JsValue> fn
                      ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isObj()) {

                JsValue mapped = fn.apply(
                        value.toJsObj()
                                         );
                json = json.set(i,
                                mapped.isObj() ?
                                        OpMapObjObjs.map(mapped.toJsObj(),
                                                         fn
                                                        ) :
                                        value
                               );
            } else if (value.isArray()) {
                json = json.set(i,
                                map(value.toJsArray(),
                                    fn
                                   )
                               );
            }
        }
        return json;
    }
}
