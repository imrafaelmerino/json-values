package jsonvalues;

import java.util.function.BiPredicate;

final class OpFilterArrKeys extends OpFilterKeys<JsArray> {


    OpFilterArrKeys(final JsArray json) {
        super(json);
    }

    @Override
    JsArray filterAll(final JsPath startingPath,
                      final BiPredicate<? super JsPath, ? super JsValue> predicate
                     ) {
        for (int i = json.size() - 1; i >= 0; i--) {

            JsValue value = json.get(i);

            if (value.isObj()) {
                final JsPath headPath = startingPath.index(i);
                json = json.set(i,
                                new OpFilterObjKeys(value.toJsObj()).filterAll(headPath,
                                                                               predicate
                                                                              )
                               );
            }
            else if (value.isArray()) {
                final JsPath headPath = startingPath.index(i);
                json = json.set(i,
                                new OpFilterArrKeys(value.toJsArray()).filterAll(headPath.index(-1),
                                                                                 predicate
                                                                                )
                               );

            }


        }
        return json;

    }

    @Override
    JsArray filter(final BiPredicate<? super JsPath, ? super JsValue> predicate) {
        throw InternalError.opNotSupportedForArrays();
    }


}
