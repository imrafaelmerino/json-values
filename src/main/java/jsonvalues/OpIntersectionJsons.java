package jsonvalues;

final class OpIntersectionJsons
{

    //squid:S1452 -> private method not exposed to the user. the wildcard allows to refactor some code, and Json<?> has only two possible types: JsObj or JsArr
    //squid:S00100 ->  naming convention: xx_ traverses the whole json
    //squid:S00117 -> ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S1452", "squid:S00117"})
    Json<?> intersectionAll(final Json<?> a,
                            final Json<?> b,
                            final JsArray.TYPE ARRAY_AS
                           )
    {
        if (a.isObj() && b.isObj()) return a.asJsObj()
                                            .intersectionAll(b.asJsObj(),
                                                             ARRAY_AS
                                                            );
        if (ARRAY_AS == JsArray.TYPE.LIST) return a.asJsArray()
                                                   .intersectionAll(b.asJsArray());
        return a.asJsArray()
                .intersection(b.asJsArray(),
                              ARRAY_AS
                             );
    }
}
