package jsonvalues;

import io.vavr.Tuple2;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterObjElems extends OpFilterElems<JsObj> {
    OpFilterObjElems(final JsObj a) {
        super(a);
    }

    @Override
    JsObj filterAll(final JsPath startingPath,
                    final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                   ) {
        for (final Tuple2<String, JsValue> next : json) {
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
                                    headElem.toJsPrimitive()
                                   )) {

                json = json.delete(next._1);
            }

        }

        return json;

    }

    @Override
    JsObj filter(final JsPath startingPath,
                 final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                ) {
        for (final Tuple2<String, JsValue> next : json) {
            JsValue headElem = next._2;
            if (headElem.isPrimitive()) {
                final JsPath headPath = startingPath.key(next._1);
                if (predicate.negate()
                             .test(headPath,
                                   headElem.toJsPrimitive()
                                  )) {

                    json = json.delete(next._1);
                }
            }

        }

        return json;
    }

    @Override
    JsObj filterAll(final Predicate<? super JsPrimitive> predicate) {
        for (final Tuple2<String, JsValue> next : json) {

            JsValue headElem = next._2;

            if (headElem.isObj()) {
                json = json.set(next._1,
                                new OpFilterObjElems(headElem.toJsObj())
                                        .filterAll(predicate)
                               );
            }
            else if (headElem.isArray()) {
                json = json.set(next._1,
                                new OpFilterArrElems(headElem.toJsArray())
                                        .filterAll(predicate)
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

    @Override
    JsObj filter(final Predicate<? super JsPrimitive> predicate) {
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
