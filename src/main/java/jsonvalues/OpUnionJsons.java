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
                                            .unionAll(b.toJsObj(),
                                                      ARRAY_AS
                                                     );
        if (ARRAY_AS == JsArray.TYPE.LIST) return a.toJsArray()
                                                   .unionAll(b.toJsArray()
                                                            );
        return a.toJsArray()
                .union(b.toJsArray(),
                       ARRAY_AS
                      );
    }
}
