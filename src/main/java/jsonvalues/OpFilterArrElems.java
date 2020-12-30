package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterArrElems {


    static JsArray filter(JsArray json,
                          final BiPredicate<? super Integer, ? super JsPrimitive> predicate
                         ) {

        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue value = json.get(i);
            if (value.isPrimitive()) {
                if (predicate.negate()
                             .test(i,
                                   value.toJsPrimitive()
                                  )) json = json.delete(i);
            }

        }
        return json;
    }


    static JsArray filterAll(JsArray json,
                             final Predicate<? super JsPrimitive> predicate) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                OpFilterObjElems.filterAll(value.toJsObj(),
                                                           predicate
                                                          )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                filterAll(value.toJsArray(),
                                          predicate
                                         )
                               );
            }
            else if (predicate.negate()
                              .test(
                                      value.toJsPrimitive()
                                   )) {
                json = json.delete(i);
            }
        }

        return json;
    }

    static JsArray filter(JsArray json,
                          final Predicate<? super JsPrimitive> predicate) {

        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue value = json.get(i);
            if (value.isPrimitive()) {
                if (predicate.negate()
                             .test(value.toJsPrimitive())) json = json.delete(i);
            }

        }
        return json;
    }

    static JsArray filterAll(JsArray json,
                             final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                            ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            final JsPath headPath = startingPath.index(i);
            JsValue      value    = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                OpFilterObjElems.filterAll(value.toJsObj(),
                                                           headPath,
                                                           predicate
                                                          )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                filterAll(value.toJsArray(),
                                          headPath,
                                          predicate
                                         )
                               );
            }
            else if (predicate.negate()
                              .test(headPath,
                                    value.toJsPrimitive()
                                   )) {
                json = json.delete(i);
            }
        }

        return json;
    }

}
