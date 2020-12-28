package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrElems extends OpMapElems<JsArray> {
    OpMapArrElems(final JsArray json) {
        super(json);
    }

    @Override
    JsArray map(final BiFunction<? super JsPath,? super  JsPrimitive, ? extends JsValue> fn,
                final JsPath startingPath
               ) {
        if (json.isEmpty()) return json;
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);
            if (value.isPrimitive()) {
                final JsPath headPath = startingPath.index(i);
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

    @Override
    JsArray mapAll(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                   final JsPath startingPath
                  ) {
        for (int i = json.size() - 1; i >= 0; i--) {
            final JsPath headPath = startingPath.index(i);
            JsValue      value    = json.get(i);
            if (value.isObj()) {
                json = json.set(i,
                                new OpMapObjElems(value.toJsObj()).mapAll(fn,
                                                                          headPath
                                                                         )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpMapArrElems(value.toJsArray()).mapAll(fn,
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

    @Override
    JsArray mapAll(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue      value    = json.get(i);
            if (value.isObj()) {
                json = json.set(i,
                                new OpMapObjElems(value.toJsObj()).mapAll(fn)
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpMapArrElems(value.toJsArray()).mapAll(fn
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

    @Override
    JsArray map(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        if (json.isEmpty()) return json;
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

