package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterArrObjs {


    static JsArray filter(JsArray json,
                          final BiPredicate<? super Integer, ? super JsObj> predicate
                         ) {

        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue value = json.get(i);
            if (value.isObj()) {
                if (predicate.negate()
                             .test(i,
                                   value.toJsObj()
                                  )) json = json.delete(i);
            }

        }

        return json;

    }

    static JsArray filterAll(JsArray json,
                             final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate

                            ) {
        for (int i = json.size() - 1; i >= 0; i--) {


            JsValue value = json.get(i);

            if (value.isObj()) {
                JsPath path = startingPath.index(i);
                if (predicate.negate()
                             .test(path,
                                   value.toJsObj()
                                  )) json = json.delete(i);
                else json = json.set(i,
                                     OpFilterObjObjs.filterAll(value.toJsObj(),
                                                               path,
                                                               predicate
                                                              )
                                    );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                filterAll(value.toJsArray(),
                                          startingPath.index(i),
                                          predicate
                                         )
                               );
            }

        }

        return json;


    }

    static JsArray filter(JsArray json,
                          final Predicate<? super JsObj> predicate) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                if (predicate.negate()
                             .test(value.toJsObj()
                                  )) json = json.delete(i);
            }

        }

        return json;
    }

    static JsArray filterAll(JsArray json,
                             final Predicate<? super JsObj> predicate) {
        for (int i = json.size() - 1; i >= 0; i--) {


            JsValue value = json.get(i);

            if (value.isObj()) {
                if (predicate.negate()
                             .test(
                                     value.toJsObj()
                                  )) json = json.delete(i);
                else json = json.set(i,
                                     OpFilterObjObjs.filterAll(value.toJsObj(),
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

        }

        return json;
    }


}
