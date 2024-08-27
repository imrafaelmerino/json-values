package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

final class OpMapArrKeys {

  private OpMapArrKeys() {
  }


  static JsArray map(JsArray json,
                     final BiFunction<? super JsPath, ? super JsValue, String> fn,
                     final JsPath startingPath
                    ) {
    JsPath headPath = startingPath;
    for (int i = 0; i < json.size(); i++) {
      headPath = headPath.inc();
      JsValue value = json.get(i);
      if (value.isObj()) {
        json = json.set(i,
                        OpMapObjKeys.map(value.toJsObj(),
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
      }


    }
    return json;

  }

  static JsArray map(JsArray json,
                     final Function<? super String, String> fn
                    ) {
    for (int i = 0; i < json.size(); i++) {
      JsValue value = json.get(i);
      if (value.isObj()) {
        json = json.set(i,
                        OpMapObjKeys.map(value.toJsObj(),
                                         fn
                                        )
                       );
      } else if (value.isArray()) {
        json = json.set(i,
                        map(value.toJsArray(),
                            fn
                           )
                       );
      }


    }
    return json;
  }
}
