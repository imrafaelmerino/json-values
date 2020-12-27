package jsonvalues;

import java.util.function.BiPredicate;

final class OpFilterArrObjs extends OpFilterObjs<JsArray> {


    OpFilterArrObjs(final JsArray json) {
        super(json);
    }

    @Override
    JsArray filter(final JsPath startingPath,
                   final BiPredicate<? super JsPath, ? super JsObj> predicate
                  ) {

        for (int i = json.size() - 1; i >= 0; i--) {


            JsValue value = json.get(i);

            if (value.isObj()) {
                JsPath path = startingPath.index(i);
                if (predicate.negate()
                             .test(path,
                                   value.toJsObj()
                                  )) json = json.delete(i);
            }

        }

        return json;

    }

    @Override
    JsArray filterAll(final JsPath startingPath,
                      final BiPredicate<? super JsPath, ? super JsObj> predicate

                     ) {
        for (int i = json.size() - 1; i >= 0; i--) {


            JsValue value = json.get(i);

            if (value.isObj()) {
                JsPath path = startingPath.index(i);
                if (predicate.negate()
                             .test(path,
                                   value.toJsObj()
                                  )) json = json.delete(i);
                else json = json.set(i,
                                     new OpFilterObjObjs(value.toJsObj()).filterAll(path,
                                                                                    predicate
                                                                                   )
                                    );
            }
            else if (value.isArray()) {
                json = json.set(i,
                                new OpFilterArrObjs(value.toJsArray()).filterAll(startingPath.index(i),
                                                                                 predicate
                                                                                )
                               );
            }

        }

        return json;


    }


}
