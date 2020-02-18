package jsonvalues;


final class OpUnionJsons
{
    // squid:S1452: Json<?> has only two possible types: JsObj or JsArr,
    // squid:S00100: naming convention: xx_ traverses the whole json
    // squid:S00117: ARRAY_AS  should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S1452", "squid:S00117"})
    Json<?> unionAll(final Json<?> a,
                     final Json<?> b,
                     final JsArray.TYPE ARRAY_AS
                    )
    {

        if (a.isObj() && b.isObj()) return a.asJsObj()
                                            .unionAll(b.asJsObj(),
                                                      ARRAY_AS
                                                     );
        if (ARRAY_AS == JsArray.TYPE.LIST) return a.asJsArray()
                                                   .unionAll(b.asJsArray()
                                                            );
        return a.asJsArray()
                .union(b.asJsArray(),
                       ARRAY_AS
                      );
    }
}
