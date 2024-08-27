package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class OpFilterArrObjs {

  private OpFilterArrObjs() {
  }


  static JsArray filter(JsArray json,
                        JsPath startingPath,
                        BiPredicate<? super JsPath, ? super JsObj> predicate

                       ) {
    for (int i = json.size() - 1; i >= 0; i--) {
      JsValue value = json.get(i);
      JsPath path = startingPath.index(i);
      if (value.isObj()) {
        if (predicate.negate()
                     .test(path,
                           value.toJsObj()
                          )) {
          json = json.delete(i);
        } else {
          json = json.set(i,
                          OpFilterObjObjs.filter(value.toJsObj(),
                                                 path,
                                                 predicate
                                                )
                         );
        }
      } else if (value.isArray()) {
        json = json.set(i,
                        filter(value.toJsArray(),
                               path,
                               predicate
                              )
                       );
      }

    }

    return json;


  }

  static JsArray filter(JsArray json,
                        Predicate<? super JsObj> predicate
                       ) {
    for (int i = json.size() - 1; i >= 0; i--) {
      JsValue value = json.get(i);
      if (value.isObj()) {
        if (predicate.negate()
                     .test(value.toJsObj())) {
          json = json.delete(i);
        } else {
          json = json.set(i,
                          OpFilterObjObjs.filter(value.toJsObj(),
                                                 predicate
                                                )
                         );
        }
      } else if (value.isArray()) {
        json = json.set(i,
                        filter(value.toJsArray(),
                               predicate
                              )
                       );
      }

    }

    return json;
  }


}
