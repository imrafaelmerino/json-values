package jsonvalues;

final class OpIntersectionJsons {

    private OpIntersectionJsons() {
    }


    //squid:S1452 -> private method not exposed to the user. the wildcard allows to refactor some code, and Json<?> has only two possible types: JsObj or JsArr
    //squid:S00117 -> ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S1452", "squid:S00117"})
    static Json<?> intersectionAll(final Json<?> a,
                                   final Json<?> b,
                                   final JsArray.TYPE ARRAY_AS
    ) {
        if (a.isObj() && b.isObj()) return a.toJsObj()
                                            .intersection(b.toJsObj(),
                                                          ARRAY_AS
                                            );
        if (ARRAY_AS == JsArray.TYPE.LIST)
            return a.toJsArray()
                    .intersection(b.toJsArray(),
                                  JsArray.TYPE.LIST);

        if (ARRAY_AS == JsArray.TYPE.SET)
            return intersectionAsSet(a.toJsArray(),
                                     b.toJsArray()
            );

        if (ARRAY_AS == JsArray.TYPE.MULTISET)
            return intersectionAsMultiSet(a.toJsArray(),
                                          b.toJsArray()
            );

        throw JsValuesInternalError.arrayOptionNotImplemented(ARRAY_AS.name());
    }


    private static JsArray intersectionAsMultiSet(final JsArray a,
                                                  final JsArray b
    ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (JsValue it : a) {
            if (b.containsValue(it))
                result = result.append(it);
        }

        return result;
    }

    private static JsArray intersectionAsSet(final JsArray a,
                                             final JsArray b
    ) {
        if (a.isEmpty()) return JsArray.empty();
        if (b.isEmpty()) return JsArray.empty();

        JsArray result = JsArray.empty();
        for (JsValue it : a) {
            if (b.containsValue(it) && !result.containsValue(it)) result = result.append(it);
        }
        return result;
    }
}
