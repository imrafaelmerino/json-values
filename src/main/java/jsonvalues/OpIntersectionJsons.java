package jsonvalues;

final class OpIntersectionJsons {

  private OpIntersectionJsons() {
  }


  static Json<?> intersectionAll(final Json<?> a,
                                 final Json<?> b,
                                 final JsArray.TYPE ARRAY_AS
                                ) {
    if (a.isObj() && b.isObj()) {
      return a.toJsObj()
              .intersection(b.toJsObj(),
                            ARRAY_AS
                           );
    }
    if (ARRAY_AS == JsArray.TYPE.LIST) {
      return a.toJsArray()
              .intersection(b.toJsArray(),
                            JsArray.TYPE.LIST);
    }

    if (ARRAY_AS == JsArray.TYPE.SET) {
      return intersectionAsSet(a.toJsArray(),
                               b.toJsArray()
                              );
    }

    if (ARRAY_AS == JsArray.TYPE.MULTISET) {
      return intersectionAsMultiSet(a.toJsArray(),
                                    b.toJsArray()
                                   );
    }

    throw new RuntimeException("Array type not implemented yet: " + ARRAY_AS);
  }


  private static JsArray intersectionAsMultiSet(final JsArray a,
                                                final JsArray b
                                               ) {
    if (a.isEmpty()) {
      return a;
    }
    if (b.isEmpty()) {
      return b;
    }

    JsArray result = JsArray.empty();
    for (JsValue it : a) {
      if (b.containsValue(it)) {
        result = result.append(it);
      }
    }

    return result;
  }

  private static JsArray intersectionAsSet(final JsArray a,
                                           final JsArray b
                                          ) {
    if (a.isEmpty()) {
      return JsArray.empty();
    }
    if (b.isEmpty()) {
      return JsArray.empty();
    }

    JsArray result = JsArray.empty();
    for (JsValue it : a) {
      if (b.containsValue(it) && !result.containsValue(it)) {
        result = result.append(it);
      }
    }
    return result;
  }
}
