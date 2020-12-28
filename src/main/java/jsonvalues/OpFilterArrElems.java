package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterArrElems extends OpFilterElems<JsArray> {

    OpFilterArrElems(final JsArray a) {
        super(a);
    }


    @Override
    JsArray filter(final JsPath startingPath,
                   final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                  ) {

        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue value = json.get(i);
            if (value.isPrimitive()) {
                final JsPath headPath = startingPath.index(i);
                if (predicate.negate()
                             .test(headPath,
                                   value.toJsPrimitive()
                                  )) json = json.delete(i);
            }

        }
        return json;
    }

    @Override
    JsArray filterAll(final Predicate<? super JsPrimitive> predicate) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                new OpFilterObjElems(value.toJsObj()).filterAll(
                                        predicate
                                                                               )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpFilterArrElems(value.toJsArray()).filterAll(
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

    @Override
    JsArray filter(final Predicate<? super JsPrimitive> predicate) {

        for (int i = json.size() - 1; i >= 0; i--) {
            JsValue value = json.get(i);
            if (value.isPrimitive()) {
                if (predicate.negate()
                             .test(value.toJsPrimitive())) json = json.delete(i);
            }

        }
        return json;
    }

    @Override
    JsArray filterAll(final JsPath startingPath,
                      final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                     ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            final JsPath headPath = startingPath.index(i);
            JsValue      value    = json.get(i);

            if (value.isObj()) {
                json = json.set(i,
                                new OpFilterObjElems(value.toJsObj()).filterAll(headPath,
                                                                                predicate
                                                                               )
                               );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpFilterArrElems(value.toJsArray()).filterAll(headPath,
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
