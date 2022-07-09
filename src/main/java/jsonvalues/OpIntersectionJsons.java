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
        if (ARRAY_AS == JsArray.TYPE.LIST) return a.toJsArray()
                                                   .intersection(b.toJsArray(),ARRAY_AS);
        return intersection(a.toJsArray(),
                            b.toJsArray(),
                            ARRAY_AS
        );
    }


    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private static JsArray intersection(JsArray a,
                                        JsArray b,
                                        JsArray.TYPE ARRAY_AS
    ) {
        switch (ARRAY_AS) {
            case SET:
                return intersectionAsSet(a,
                                         b
                );
            case LIST:
                return intersectionAsList(a,
                                          b
                );
            case MULTISET:
                return intersectionAsMultiSet(a,
                                              b
                );
            default:
                throw JsValuesInternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }

    }

    private static JsArray intersectionAsList(JsArray a,
                                              JsArray b
    ) {

        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (int i = 0; i < a.size(); i++) {
            JsValue aVal = a.get(i);
            JsValue bVal = b.get(i);
            if (aVal.isNothing() || bVal.isNothing()) return result;
            if (aVal.equals(bVal)) result = result.append(aVal);
        }

        return result;

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
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (JsValue it : a) {
            if (b.containsValue(it) && !result.containsValue(it)) result = result.append(it);
        }
        return result;
    }
}
