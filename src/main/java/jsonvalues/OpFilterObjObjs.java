package jsonvalues;

import io.vavr.Tuple2;

import java.util.Iterator;
import java.util.function.BiPredicate;

final class OpFilterObjObjs extends OpFilterObjs<JsObj> {


    OpFilterObjObjs(final JsObj json) {
        super(json);
    }

    @Override
    JsObj filter(final JsPath startingPath,
                 final BiPredicate<? super JsPath, ? super JsObj> predicate

                ) {
        if (json.isEmpty()) return json;
        Iterator<Tuple2<String, JsValue>> iterator = json.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, JsValue> next = iterator.next();
            if (next._2.isObj()) {
                if (predicate.negate()
                             .test(startingPath.key(next._1),
                                   next._2.toJsObj()
                                  )) {
                    json = json.delete(next._1);
                }
            }
        }
        return json;
    }

    @Override
    JsObj filterAll(final JsPath startingPath,
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
                                    new OpFilterObjObjs(next._2.toJsObj()).filterAll(path,
                                                                                     predicate
                                                                                    )
                                   );
                }
            }
            else if (next._2.isArray()) {
                JsPath path = startingPath.key(next._1);
                json = json.set(next._1,
                                new OpFilterArrObjs(next._2.toJsArray()).filterAll(path,
                                                                                   predicate
                                                                                  )
                               );
            }
        }
        return json;
    }


}
