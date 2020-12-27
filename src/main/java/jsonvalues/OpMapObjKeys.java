package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiFunction;

final class OpMapObjKeys extends OpMapKeys<JsObj> {
    OpMapObjKeys(final JsObj json) {
        super(json);
    }

    @Override
    JsObj map(final BiFunction<? super JsPath, ? super JsValue, String> fn,
              final JsPath startingPath
             ) {
        JsObj result = JsObj.empty();
        for (final Tuple2<String, JsValue> next : json) {
            final JsPath headPath = startingPath.key(next._1);
            final String keyMapped = fn.apply(headPath,
                                              next._2
                                             );
            result = result.set(keyMapped,
                                next._2
                               );

        }
        return result;
    }

    @Override
    JsObj mapAll(final BiFunction<? super JsPath, ? super JsValue, String> fn,
                 final JsPath startingPath
                ) {
        JsObj result = JsObj.empty();

        for (final Tuple2<String, JsValue> next : json) {
            final JsPath headPath = startingPath.key(next._1);
            final String keyMapped = fn.apply(headPath,
                                              next._2
                                             );
            if (next._2.isObj()) {
                result = result.set(keyMapped,
                                    new OpMapObjKeys(next._2.toJsObj()).mapAll(fn,
                                                                               headPath
                                                                              )
                                   );
            }
            else if (next._2.isArray()) {
                result = result.set(keyMapped,
                                    new OpMapArrKeys(next._2.toJsArray()).mapAll(fn,
                                                                                 headPath.index(-1)
                                                                                )
                                   );
            }
            else {
                result = result.set(keyMapped,
                                    next._2
                                   )


                ;
            }


        }
        return result;

    }
}
