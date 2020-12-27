package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiPredicate;

final class OpFilterObjKeys extends OpFilterKeys<JsObj> {

    OpFilterObjKeys(final JsObj json) {
        super(json);
    }

    @Override
    JsObj filterAll(final JsPath startingPath,
                    final BiPredicate<? super JsPath, ? super JsValue> predicate
                   ) {
        for (final Tuple2<String, JsValue> next : json) {
            final JsPath headPath = startingPath.key(next._1);

            if (predicate.negate()
                         .test(headPath,
                               next._2
                              )) {

                json = json.delete(next._1);
            }
            else if (next._2.isObj())
                json = json.set(next._1,
                                new OpFilterObjKeys(next._2.toJsObj()).filterAll(headPath,
                                                                                 predicate
                                                                                )
                               );

            else if (next._2.isArray())
                json = json.set(next._1,
                                new OpFilterArrKeys(next._2.toJsArray()).filterAll(headPath,
                                                                                   predicate
                                                                                  )
                               );


        }

        return json;

    }

    @Override
    JsObj filter(final BiPredicate<? super JsPath, ? super JsValue> predicate) {
        if (json.isEmpty()) return json;
        for (final Tuple2<String, JsValue> next : json) {
            if (predicate.negate()
                         .test(JsPath.fromKey(next._1),
                               next._2
                              )) {
                json = json.delete(next._1);

            }
        }
        return json;
    }


}
