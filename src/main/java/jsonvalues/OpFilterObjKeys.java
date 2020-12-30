package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjKeys {


    static JsObj filterAll(JsObj json,
                           final JsPath startingPath,
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
                                filterAll(next._2.toJsObj(),
                                          headPath,
                                          predicate
                                         )
                               );

            else if (next._2.isArray())
                json = json.set(next._1,
                                OpFilterArrKeys.filterAll(next._2.toJsArray(),
                                                          headPath,
                                                          predicate
                                                         )
                               );


        }

        return json;

    }

    static JsObj filter(JsObj json,
                        final BiPredicate<? super String, ? super JsValue> predicate) {
        for (final Tuple2<String, JsValue> next : json) {
            if (predicate.negate()
                         .test(next._1,
                               next._2
                              )) {
                json = json.delete(next._1);

            }
        }
        return json;
    }

    static JsObj filterAll(JsObj json,
                           final Predicate<? super String> predicate) {
        for (final Tuple2<String, JsValue> next : json) {

            if (predicate.negate()
                         .test(next._1)) {

                json = json.delete(next._1);
            }
            else if (next._2.isObj())
                json = json.set(next._1,
                                filterAll(next._2.toJsObj(),
                                          predicate
                                         )
                               );

            else if (next._2.isArray())
                json = json.set(next._1,
                                OpFilterArrKeys.filterAll(next._2.toJsArray(),
                                                          predicate
                                                         )
                               );


        }

        return json;
    }

    static JsObj filter(JsObj json,
                        final Predicate<? super String> predicate) {
        for (final Tuple2<String, JsValue> next : json) {
            if (predicate.negate()
                         .test(next._1
                              )) {
                json = json.delete(next._1);

            }
        }
        return json;
    }


}
