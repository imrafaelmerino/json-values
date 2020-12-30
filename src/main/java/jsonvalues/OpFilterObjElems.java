package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjElems {

    static JsObj filterAll(JsObj json,
                           final JsPath startingPath,
                           final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                          ) {
        for (final Tuple2<String, JsValue> next : json) {
            final JsPath headPath = startingPath.key(next._1);

            JsValue headElem = next._2;

            if (headElem.isObj()) {
                json = json.set(next._1,
                                filterAll(headElem.toJsObj(),
                                          headPath,
                                          predicate
                                         )
                               );
            }
            else if (headElem.isArray()) {
                json = json.set(next._1,
                                OpFilterArrElems
                                        .filterAll(headElem.toJsArray(),
                                                   headPath,
                                                   predicate
                                                  )
                               );
            }

            else if (predicate.negate()
                              .test(headPath,
                                    headElem.toJsPrimitive()
                                   )) {

                json = json.delete(next._1);
            }

        }

        return json;

    }

    static JsObj filter(JsObj json,
                        final BiPredicate<? super String, ? super JsPrimitive> predicate
                       ) {
        for (final Tuple2<String, JsValue> next : json) {
            JsValue headElem = next._2;
            if (headElem.isPrimitive()) {
                if (predicate.negate()
                             .test(next._1,
                                   headElem.toJsPrimitive()
                                  )) {

                    json = json.delete(next._1);
                }
            }

        }

        return json;
    }

    static JsObj filterAll(JsObj json,
                           final Predicate<? super JsPrimitive> predicate) {
        for (final Tuple2<String, JsValue> next : json) {

            JsValue headElem = next._2;

            if (headElem.isObj()) {
                json = json.set(next._1,
                                filterAll(headElem.toJsObj(),
                                          predicate
                                         )
                               );
            }
            else if (headElem.isArray()) {
                json = json.set(next._1,
                                OpFilterArrElems
                                        .filterAll(headElem.toJsArray(),
                                                   predicate
                                                  )
                               );
            }

            else if (predicate.negate()
                              .test(headElem.toJsPrimitive()
                                   )) {

                json = json.delete(next._1);
            }

        }

        return json;
    }

    static JsObj filter(JsObj json,
                        final Predicate<? super JsPrimitive> predicate) {
        for (final Tuple2<String, JsValue> next : json) {
            JsValue headElem = next._2;
            if (headElem.isPrimitive()) {
                if (predicate.negate()
                             .test(headElem.toJsPrimitive()
                                  )) {

                    json = json.delete(next._1);
                }
            }

        }

        return json;
    }
}
