package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterArrKeys {

    private OpFilterArrKeys() {
    }

    static JsArray filter(JsArray json,
                          JsPath startingPath,
                          BiPredicate<? super JsPath, ? super JsValue> predicate
                         ) {
        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue value = json.get(i);
            JsPath headPath = startingPath.index(i);
            if (value.isObj()) {
                json = json.set(i,
                                OpFilterObjKeys.filter(value.toJsObj(),
                                                       headPath,
                                                       predicate
                                                      )
                               );
            } else if (value.isArray()) {
                json = json.set(i,
                                filter(value.toJsArray(),
                                       headPath,
                                       predicate
                                      )
                               );
            }
        }
        return json;

    }


    static JsArray filter(JsArray json,
                          Predicate<? super String> predicate
                         ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                OpFilterObjKeys.filter(value.toJsObj(),
                                                       predicate)
                               );
            } else if (value.isArray()) {
                json = json.set(i,
                                filter(value.toJsArray(),
                                       predicate)
                               );

            }


        }
        return json;
    }


}
