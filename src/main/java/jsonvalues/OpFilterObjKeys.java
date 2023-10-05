package jsonvalues;


import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjKeys {

    private OpFilterObjKeys() {
    }

    static JsObj filter(JsObj json,
                        JsPath startingPath,
                        BiPredicate<? super JsPath, ? super JsValue> predicate) {
        for (JsObjPair next : json) {
            JsPath headPath = startingPath.key(next.key());

            if (predicate.negate().test(headPath,
                                        next.value())) {

                json = json.delete(next.key());
            } else if (next.value().isObj())
                json = json.set(next.key(),
                                filter(next.value().toJsObj(),
                                       headPath,
                                       predicate));

            else if (next.value().isArray())
                json = json.set(next.key(),
                                OpFilterArrKeys.filter(next.value().toJsArray(),
                                                       headPath,
                                                       predicate));


        }

        return json;

    }


    static JsObj filter(JsObj json,
                        Predicate<? super String> predicate) {
        for (JsObjPair next : json) {

            if (predicate.negate().test(next.key())) {

                json = json.delete(next.key());
            } else if (next.value().isObj())
                json = json.set(next.key(),
                                filter(next.value().toJsObj(),
                                       predicate));

            else if (next.value().isArray())
                json = json.set(next.key(),
                                OpFilterArrKeys.filter(next.value().toJsArray(),
                                                       predicate));


        }

        return json;
    }


}
