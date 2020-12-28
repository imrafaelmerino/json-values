package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrObjs extends OpMapObjs<JsArray> {
    OpMapArrObjs(final JsArray json) {
        super(json);
    }

    @Override
    JsArray map(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                final JsPath startingPath
               ) {

        for (int i = json.size() - 1; i >= 0; i--) {
            final JsPath headPath = startingPath.index(i);

            JsValue value = json.get(i);
            if (value.isObj()) {

                json = json.set(i,
                                fn.apply(headPath,
                                         value.toJsObj()
                                        )
                               );
            }
        }
        return json;
    }

    @Override
    JsArray mapAll(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
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
                                new OpMapObjObjs(mapped.toJsObj()).mapAll(fn,
                                                                          headPath
                                                                         ) :
                                value
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpMapArrObjs(value.toJsArray())
                                        .mapAll(fn,
                                                headPath
                                               )
                               );
            }
        }
        return json;

    }

    @Override
    JsArray map(final Function<? super JsObj, JsValue> fn) {

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

    @Override
    JsArray mapAll(final Function<? super JsObj, JsValue> fn) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isObj()) {

                JsValue mapped = fn.apply(
                                          value.toJsObj()
                                         );
                json = json.set(i,
                                mapped.isObj() ?
                                new OpMapObjObjs(mapped.toJsObj()).mapAll(fn
                                                                         ) :
                                value
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpMapArrObjs(value.toJsArray())
                                        .mapAll(fn
                                               )
                               );
            }
        }
        return json;
    }
}
