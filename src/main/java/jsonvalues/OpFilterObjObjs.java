package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjObjs {

    private OpFilterObjObjs() {
    }


    static JsObj filter(JsObj json,
                        JsPath startingPath,
                        BiPredicate<? super JsPath, ? super JsObj> predicate) {
        for (Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {
                JsPath path = startingPath.key(next._1);
                if (predicate.negate().test(path,
                                            next._2.toJsObj())) {
                    json = json.delete(next._1);
                } else {
                    json = json.set(next._1,
                                    filter(next._2.toJsObj(),
                                           path,
                                           predicate));
                }
            } else if (next._2.isArray()) {
                JsPath path = startingPath.key(next._1);
                json = json.set(next._1,
                                OpFilterArrObjs.filter(next._2.toJsArray(),
                                                       path,
                                                       predicate));
            }
        }
        return json;
    }


    static JsObj filter(JsObj json,
                        Predicate<? super JsObj> predicate) {
        for (Tuple2<String, JsValue> next : json) {
            if (next._2.isObj()) {
                if (predicate.negate().test(next._2.toJsObj())) {
                    json = json.delete(next._1);
                } else {
                    json = json.set(next._1,
                                    filter(next._2.toJsObj(),
                                           predicate));
                }
            } else if (next._2.isArray()) {
                json = json.set(next._1,
                                OpFilterArrObjs.filter(next._2.toJsArray(),
                                                       predicate));
            }
        }
        return json;
    }


}
