package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrKeys extends OpMapKeys<JsArray> {
    OpMapArrKeys(final JsArray json) {
        super(json);
    }

    @Override
    JsArray map(final BiFunction<? super JsPath, ? super JsValue, String> fn,
                final JsPath startingPath
               ) {
        throw InternalError.opNotSupportedForArrays();
    }

    @Override
    JsArray map(final Function<? super String, String> fn) {
        throw InternalError.opNotSupportedForArrays();

    }

    @Override
    JsArray mapAll(final BiFunction<? super JsPath, ? super JsValue, String> fn,
                   final JsPath startingPath
                  ) {
        JsPath headPath = startingPath;
        for (int i = 0; i < json.size(); i++) {
            headPath = headPath.inc();
            JsValue value = json.get(i);
            if (value.isObj()) {
                json = json.set(i,
                                new OpMapObjKeys(value.toJsObj()).mapAll(fn,
                                                                         headPath
                                                                        )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpMapArrKeys(value.toJsArray()).mapAll(fn,
                                                                           headPath.index(-1)
                                                                          )
                               );
            }


        }
        return json;

    }

    @Override
    JsArray mapAll(final Function<? super String, String> fn) {
        for (int i = 0; i < json.size(); i++) {
            JsValue value = json.get(i);
            if (value.isObj()) {
                json = json.set(i,
                                new OpMapObjKeys(value.toJsObj()).mapAll(fn)
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpMapArrKeys(value.toJsArray()).mapAll(fn)
                               );
            }


        }
        return json;
    }
}
