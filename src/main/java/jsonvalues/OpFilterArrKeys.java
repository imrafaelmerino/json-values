package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterArrKeys {

    private OpFilterArrKeys(){}

    static JsArray filterAll(JsArray json,
                             final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsValue> predicate
                            ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                final JsPath headPath = startingPath.index(i);
                json = json.set(i,
                                OpFilterObjKeys.filterAll(value.toJsObj(),
                                                          headPath,
                                                          predicate
                                                         )
                               );
            }
            else if (value.isArray()) {
                final JsPath headPath = startingPath.index(i);
                json = json.set(i,
                                filterAll(value.toJsArray(),
                                          headPath.index(-1),
                                          predicate
                                         )
                               );

            }


        }
        return json;

    }


    static JsArray filterAll(JsArray json,
                             final Predicate<? super String> predicate) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                OpFilterObjKeys.filterAll(value.toJsObj(),
                                                          predicate)
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                filterAll(value.toJsArray(),
                                          predicate)
                               );

            }


        }
        return json;
    }


}
