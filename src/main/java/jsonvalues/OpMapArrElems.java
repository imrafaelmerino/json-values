package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrElems {

  private OpMapArrElems() {
  }


  static JsArray map(JsArray json,
                     final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                     final JsPath startingPath
                    ) {
    JsPath headPath = startingPath;
    for (int i = 0; i < json.size(); i++) {
      headPath = headPath.inc();
      JsValue value = json.get(i);
      if (value.isObj()) {
        json = json.set(i,
                        OpMapObjElems.map(value.toJsObj(),
                                          fn,
                                          headPath
                                         )
                       );
      } else if (value.isArray()) {
        json = json.set(i,
                        map(value.toJsArray(),
                            fn,
                            headPath.index(-1)
                           )
                       );
      } else {

        JsValue headMapped = fn.apply(headPath,
                                      value.toJsPrimitive()
                                     );
        json = json.set(i,
                        headMapped
                       );
      }

    }

    return json;

  }

  static JsArray map(JsArray json,
                     final Function<? super JsPrimitive, ? extends JsValue> fn
                    ) {
    for (int i = 0; i < json.size(); i++) {
      JsValue value = json.get(i);
      if (value.isObj()) {
        json = json.set(i,
                        OpMapObjElems.map(value.toJsObj(),
                                          fn
                                         )
                       );
      } else if (value.isArray()) {
        json = json.set(i,
                        OpMapArrElems.map(value.toJsArray(),
                                          fn
                                         )
                       );
      } else {

        JsValue headMapped = fn.apply(value.toJsPrimitive());
        json = json.set(i,
                        headMapped
                       );
      }

    }

    return json;
  }


}

