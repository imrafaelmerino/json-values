package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapObjElems extends OpMapElems<JsObj> {
    OpMapObjElems(final JsObj json) {
        super(json);
    }

    @Override
    JsObj map(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
              final JsPath startingPath
             ) {
        if (json.isEmpty()) return json;
        for (final Tuple2<String, JsValue> tuple : json) {

            if (tuple._2.isPrimitive()) {
                final JsPath headPath = startingPath.key(tuple._1);

                JsValue headMapped = fn.apply(headPath,
                                              tuple._2.toJsPrimitive()
                                             );
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;


    }

    @Override
    JsObj map(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        if (json.isEmpty()) return json;
        for (final Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isPrimitive()) {
                JsValue headMapped = fn.apply(tuple._2.toJsPrimitive());
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;


    }

    @Override
    JsObj mapAll(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                 final JsPath startingPath
                ) {
        if (json.isEmpty()) return json;
        for (final Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isObj()) {
                json = json.set(tuple._1,
                                new OpMapObjElems(tuple._2.toJsObj()).mapAll(fn,
                                                                             startingPath.key(tuple._1)
                                                                            )
                               );
            }
            else if (tuple._2.isArray()) {
                json = json.set(tuple._1,
                                new OpMapArrElems(tuple._2.toJsArray()).mapAll(fn,
                                                                               startingPath.key(tuple._1)
                                                                              )
                               );
            }
            else {
                final JsPath headPath = startingPath.key(tuple._1);

                JsValue headMapped = fn.apply(headPath,
                                              tuple._2.toJsPrimitive()
                                             );
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;

    }

    @Override
    JsObj mapAll(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        if (json.isEmpty()) return json;
        for (final Tuple2<String, JsValue> tuple : json) {
            if (tuple._2.isObj()) {
                json = json.set(tuple._1,
                                new OpMapObjElems(tuple._2.toJsObj()).mapAll(fn)
                               );
            }
            else if (tuple._2.isArray()) {
                json = json.set(tuple._1,
                                new OpMapArrElems(tuple._2.toJsArray()).mapAll(fn)
                               );
            }
            else {

                JsValue headMapped = fn.apply(tuple._2.toJsPrimitive());
                json = json.set(tuple._1,
                                headMapped
                               );
            }
        }

        return json;
    }
}
