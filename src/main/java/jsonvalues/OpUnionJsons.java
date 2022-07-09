package jsonvalues;


final class OpUnionJsons {
    // squid:S1452: Json<?> has only two possible types: JsObj or JsArr,
    // squid:S00117: ARRAY_AS  should be a valid name
    @SuppressWarnings({"squid:S1452", "squid:S00117"})
    static Json<?> unionAll(final Json<?> a,
                            final Json<?> b,
                            final JsArray.TYPE ARRAY_AS
    ) {

        if (a.isObj() && b.isObj()) return a.toJsObj()
                                            .union(b.toJsObj(),
                                                   ARRAY_AS
                                            );
        if (ARRAY_AS == JsArray.TYPE.LIST) return a.toJsArray()
                                                   .union(b.toJsArray(),ARRAY_AS
                                                   );
        return union(a.toJsArray(),
                     b.toJsArray(),
                     ARRAY_AS
        );
    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private static JsArray union(JsArray a,
                                 JsArray b,
                                 JsArray.TYPE ARRAY_AS
    ) {
        switch (ARRAY_AS) {
            case SET:
                return unionAsSet(a,
                                  b
                );
            case LIST:
                return unionAsList(a,
                                   b
                );
            case MULTISET:
                return unionAsMultiSet(a,
                                       b
                );
            default:
                throw JsValuesInternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }
    }

    private static JsArray unionAsList(final JsArray a,
                                       final JsArray b
    ) {
        if (a.isEmpty()) return b;
        JsArray result = a;
        for (int i = a.size(); i < b.size(); i++) {
            result = result.append(b.get(i));
        }
        return result;
    }

    private static JsArray unionAsMultiSet(final JsArray a,
                                           final JsArray b
    ) {
        return a.appendAll(b);

    }

    private static JsArray unionAsSet(final JsArray a,
                                      final JsArray b
    ) {
        if (b.isEmpty()) return a;
        if (a.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (final JsValue value : a) {
            if (!result.containsValue(value)) result = result.append(value);
        }
        for (final JsValue value : b) {
            if (!result.containsValue(value)) result = result.append(value);
        }

        return result;
    }

}
