package jsonvalues;

import io.vavr.Tuple2;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjObjs extends OpMapObjs<JsObj> {
    OpMapObjObjs(final JsObj json) {
        super(json);
    }

    @Override
    JsObj map(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
              final JsPath startingPath
             ) {
        for (final Tuple2<String, JsValue> next : json) {
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
        for (final Tuple2<String, JsValue> next : json) {
            final JsPath headPath = startingPath.key(next._1);

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

    @Override
    JsObj map(final Function<? super JsObj, JsValue> fn) {
        for (final Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {

                json = json.set(next._1,
                                fn.apply(
                                         next._2.toJsObj()
                                        )
                               );
            }
        }
        return json;
    }

    @Override
    JsObj mapAll(final Function<? super JsObj, JsValue> fn) {
        for (final Tuple2<String, JsValue> next : json) {

            if (next._2.isObj()) {
                JsValue mapped = fn.apply(
                                          next._2.toJsObj()
                                         );
                json = json.set(next._1,
                                mapped.isObj() ?
                                new OpMapObjObjs(mapped.toJsObj()).mapAll(fn
                                                                         )
                                               : mapped
                               );
            }
            else if (next._2.isArray()) {
                json = json.set(next._1,
                                new OpMapArrObjs(next._2.toJsArray()).mapAll(fn
                                                                            )
                               );
            }

        }
        return json;

    }
}
