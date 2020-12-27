package jsonvalues;

import io.vavr.Tuple2;

import java.util.Iterator;
import java.util.function.BiPredicate;

final class OpFilterObjElems extends OpFilterElems<JsObj> {
    OpFilterObjElems(final JsObj a) {
        super(a);
    }

    @Override
    JsObj filterAll(final JsPath startingPath,
                    final BiPredicate<? super JsPath, ? super JsValue> predicate
                   ) {
        Iterator<Tuple2<String, JsValue>> iterator = json.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, JsValue> next = iterator.next();

            final JsPath headPath = startingPath.key(next._1);

            JsValue headElem = next._2;

            if (headElem.isObj()) {
                json = json.set(next._1,
                                new OpFilterObjElems(headElem.toJsObj())
                                        .filterAll(headPath,
                                                   predicate
                                                  )
                               );
            }
            else if (headElem.isArray()) {
                json = json.set(next._1,
                                new OpFilterArrElems(headElem.toJsArray())
                                        .filterAll(headPath,
                                                   predicate
                                                  )
                               );
            }

            else if (predicate.negate()
                              .test(headPath,
                                    headElem
                                   )) {

                json = json.delete(next._1);
            }

        }

        return json;

    }

    @Override
    JsObj filter(final JsPath startingPath,
                 final BiPredicate<? super JsPath, ? super JsValue> predicate
                ) {
        Iterator<Tuple2<String, JsValue>> iterator = json.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, JsValue> next     = iterator.next();
            JsValue                 headElem = next._2;
            if (headElem.isPrimitive()) {
                final JsPath headPath = startingPath.key(next._1);
                if (predicate.negate()
                             .test(headPath,
                                   headElem
                                  )) {

                    json = json.delete(next._1);
                }
            }

        }

        return json;
    }
}
