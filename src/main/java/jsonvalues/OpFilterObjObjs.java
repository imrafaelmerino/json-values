package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjObjs {


    static JsObj filter(JsObj json,
                        final BiPredicate<? super String, ? super JsObj> predicate

                       ) {
        for (final Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {
                if (predicate.negate()
                             .test(next._1,
                                   next._2.toJsObj()
                                  )) {
                    json = json.delete(next._1);
                }
            }
        }
        return json;
    }

    static JsObj filterAll(JsObj json,
                           final JsPath startingPath,
                           final BiPredicate<? super JsPath, ? super JsObj> predicate
                          ) {
        for (final Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {
                JsPath path = startingPath.key(next._1);
                if (predicate.negate()
                             .test(path,
                                   next._2.toJsObj()
                                  )) {
                    json = json.delete(next._1);
                }
                else {
                    json = json.set(next._1,
                                    filterAll(next._2.toJsObj(),
                                              path,
                                              predicate
                                             )
                                   );
                }
            }
            else if (next._2.isArray()) {
                JsPath path = startingPath.key(next._1);
                json = json.set(next._1,
                                OpFilterArrObjs.filterAll(next._2.toJsArray(),
                                                          path,
                                                          predicate
                                                         )
                               );
            }
        }
        return json;
    }

    static JsObj filter(JsObj json,
                        final Predicate<? super JsObj> predicate) {
        for (final Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {
                if (predicate.negate()
                             .test(next._2.toJsObj()
                                  )) {
                    json = json.delete(next._1);
                }
            }
        }
        return json;
    }

    static JsObj filterAll(JsObj json,
                           final Predicate<? super JsObj> predicate) {
        for (final Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {
                if (predicate.negate()
                             .test(next._2.toJsObj()
                                  )) {
                    json = json.delete(next._1);
                }
                else {
                    json = json.set(next._1,
                                    filterAll(next._2.toJsObj(),
                                              predicate
                                             )
                                   );
                }
            }
            else if (next._2.isArray()) {
                json = json.set(next._1,
                                OpFilterArrObjs.filterAll(next._2.toJsArray(),
                                                          predicate
                                                         )
                               );
            }
        }
        return json;
    }


}
