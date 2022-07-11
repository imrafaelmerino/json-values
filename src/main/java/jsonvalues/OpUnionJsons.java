package jsonvalues;


class OpUnionJsons {
    // squid:S1452: Json<?> has only two possible types: JsObj or JsArr,
    // squid:S00117: ARRAY_AS  should be a valid name
    @SuppressWarnings({"squid:S1452", "squid:S00117"})
    static Json<?> unionAll(Json<?> a,
                            Json<?> b,
                            JsArray.TYPE ARRAY_AS
    ) {

        if (a.isObj() && b.isObj()) return a.toJsObj()
                                            .union(b.toJsObj(),
                                                   ARRAY_AS
                                            );
        if (ARRAY_AS == JsArray.TYPE.LIST) return a.toJsArray()
                                                   .union(b.toJsArray(),
                                                          ARRAY_AS
                                                   );
        if (ARRAY_AS == JsArray.TYPE.SET)
            return unionAsSet(a.toJsArray(),
                              b.toJsArray());

        if (ARRAY_AS == JsArray.TYPE.MULTISET)
            return unionAsMultiSet(a.toJsArray(),
                                   b.toJsArray());
        throw new  RuntimeException("Array type not implemented yet: "+ARRAY_AS);

    }


    private static JsArray unionAsMultiSet(JsArray a,
                                           JsArray b
    ) {
        return a.appendAll(b);

    }

    private static JsArray unionAsSet(JsArray a,
                                      JsArray b
    ) {
        if (b.isEmpty()) return a;
        if (a.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (JsValue value : a) {
            if (!result.containsValue(value)) result = result.append(value);
        }
        for (JsValue value : b) {
            if (!result.containsValue(value)) result = result.append(value);
        }

        return result;
    }

}
