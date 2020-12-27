package jsonvalues;

import io.vavr.Tuple2;

import java.util.Iterator;
import java.util.function.BiFunction;

final class OpMapObjObjs extends OpMapObjs<JsObj> {
    OpMapObjObjs(final JsObj json) {
        super(json);
    }

    @Override
    JsObj map(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
              final JsPath startingPath
             ) {
        if (json.isEmpty()) return json;
        Iterator<Tuple2<String, JsValue>> iterator = json.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, JsValue> next = iterator.next();
            if (next._2.isObj()) {
                final JsPath headPath = startingPath.key(next._1);

                json = json.set(next._1,
                                fn.apply(headPath,
                                         next._2.toJsObj()
                                        )
                               );
            }
        }
        return json;
    }

    @Override
    JsObj mapAll(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                 final JsPath startingPath
                ) {
        if (json.isEmpty()) return json;
        Iterator<Tuple2<String, JsValue>> iterator = json.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, JsValue> next     = iterator.next();
            final JsPath            headPath = startingPath.key(next._1);

            if (next._2.isObj()) {
                JsValue mapped = fn.apply(headPath,
                                          next._2.toJsObj()
                                         );
                json = json.set(next._1,
                                mapped.isObj() ?
                                new OpMapObjObjs(mapped.toJsObj()).mapAll(fn,
                                                                          headPath
                                                                         )
                                               : mapped
                               );
            }
            else if (next._2.isArray()) {
                json = json.set(next._1,
                                new OpMapArrObjs(next._2.toJsArray()).mapAll(fn,
                                                                             headPath
                                                                            )
                               );
            }

        }
        return json;

    }
}
