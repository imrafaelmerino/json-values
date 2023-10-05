package jsonvalues;


import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjObjs {

    private OpFilterObjObjs() {
    }


    static JsObj filter(JsObj json,
                        JsPath startingPath,
                        BiPredicate<? super JsPath, ? super JsObj> predicate) {
        for (JsObjPair next : json) {
            if (next.value().isObj()) {
                JsPath path = startingPath.key(next.key());
                if (predicate.negate().test(path,
                                            next.value().toJsObj())) {
                    json = json.delete(next.key());
                } else {
                    json = json.set(next.key(),
                                    filter(next.value().toJsObj(),
                                           path,
                                           predicate));
                }
            } else if (next.value().isArray()) {
                JsPath path = startingPath.key(next.key());
                json = json.set(next.key(),
                                OpFilterArrObjs.filter(next.value().toJsArray(),
                                                       path,
                                                       predicate));
            }
        }
        return json;
    }


    static JsObj filter(JsObj json,
                        Predicate<? super JsObj> predicate) {
        for (JsObjPair next : json) {
            if (next.value().isObj()) {
                if (predicate.negate().test(next.value().toJsObj())) {
                    json = json.delete(next.key());
                } else {
                    json = json.set(next.key(),
                                    filter(next.value().toJsObj(),
                                           predicate));
                }
            } else if (next.value().isArray()) {
                json = json.set(next.key(),
                                OpFilterArrObjs.filter(next.value().toJsArray(),
                                                       predicate));
            }
        }
        return json;
    }


}
