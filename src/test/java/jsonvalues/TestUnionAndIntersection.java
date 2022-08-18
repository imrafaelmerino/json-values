package jsonvalues;

import com.dslplatform.json.MyDslJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

public class TestUnionAndIntersection {

    @Test
    public void test_1_testUnion() {

        JsObj a = JsObj.parse("{\"a\": 1, \"c\": [{\"d\": 1}]}");

        JsObj b = JsObj.parse("{\"b\": 2, \"c\": [{\"e\": 2}]}");

        JsObj c = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1}, {\"e\": 2}]}");

        JsObj d = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1,\"e\": 2}]}");

        Assertions.assertEquals(c,
                                a.union(b,
                                        SET
                                )
        );

        Assertions.assertEquals(c,
                                a.union(b,
                                        MULTISET
                                )
        );

        Assertions.assertEquals(d,
                                a.union(b,
                                        LIST
                                )
        );


    }

    @Test
    public void test_2_testUnion() {


        JsObj a = JsObj.parse("{\"a\": [1, 2]}");

        JsObj b = JsObj.parse("{\"a\": [3, [4, 5], 6]}");

        JsObj c = JsObj.parse("{\"a\":[1,2,3,[4,5],6]}");

        JsObj d = JsObj.parse("{\"a\":[1,2,6]}");


        Assertions.assertEquals(c,
                                a.union(b,
                                        SET
                                )
        );

        Assertions.assertEquals(c,
                                a.union(b,
                                        MULTISET
                                )
        );

        Assertions.assertEquals(d,
                                a.union(b,
                                        LIST
                                )
        );



    }

    @Test
    public void test_3_testUnion() {


        JsArray a = JsArray.parse("[\"1\", \"2\"]");

        JsArray b = JsArray.parse("[\"3\", [\"4\", \"5\"], \"6\"]");

        JsArray d = JsArray.parse("[\"1\",\"2\",\"6\"]");

        Assertions.assertEquals(d,
                                a.union(b,JsArray.TYPE.LIST
                                )
        );



    }

    @Test
    public void test_4_testUnion() {


        JsObj a = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}]}");

        JsObj b = JsObj.parse("{\"a\": [3, [4, 5], 6, 7], \"b\": [1, 2]}");

        JsObj c = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 3, [4, 5], 6, 7], \"b\": [1, 2]}");

        JsObj d = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 7],\"b\": [1, 2]}");

        Assertions.assertEquals(c,
                                a.union(b,
                                        SET
                                )
        );

        Assertions.assertEquals(c,
                                a.union(b,
                                        MULTISET
                                )
        );

        Assertions.assertEquals(d,
                                a.union(b,
                                        LIST
                                )
        );


    }

    @Test
    public void test_5_testUnion() {


        JsObj a = JsObj.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]} ]}");

        JsObj b = JsObj.parse("{\"a\":\"b\", \"b\":[4,5,6], \"c\":[ {\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}");

        JsObj c = JsObj.parse("{\"a\":1, \"b\":[1,2,3,4,5,6], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]}, "
                                      + "{\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}");

        JsObj d = JsObj.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2,5]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\",\"g\"]}]}");


        Assertions.assertEquals(c,
                                a.union(b,
                                        SET
                                )
        );

        Assertions.assertEquals(c,
                                a.union(b,
                                        MULTISET
                                )
        );

        Assertions.assertEquals(d,
                                a.union(b,
                                        LIST
                                )
        );

    }

    @Test
    public void test_1_testIntersection() {


        JsObj a = JsObj.parse("{\"a\":1,\"b\":2}");

        JsObj b = JsObj.parse("{\"a\":1}");

        JsObj c = JsObj.parse("{\"a\":1}");


        Assertions.assertEquals(c,
                                a.intersection(b,
                                               LIST
                                )
        );


    }

    @Test
    public void test_2_testIntersection() {


        JsObj a = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1,2]},{\"b\":2},{\"c\":3}]}}");

        JsObj b = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}");

        JsObj c = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}");


        Assertions.assertEquals(c,
                                a.intersection(b,
                                               LIST
                                )
        );


    }


    @Test
    public void test_map_values_array() {


        JsArray array = JsArray.of(JsStr.of("A"),
                                   TRUE,
                                   JsStr.of("B")
        );

        JsArray newArray = array.mapValues((i, val) -> JsStr.prism.modify.apply(String::toLowerCase)
                                                                         .apply(val)
        );

        Assertions.assertNotEquals(array,
                                   newArray
        );

        Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                           TRUE,
                                           JsStr.of("b")
                                ),
                                newArray
        );

        JsArray array1 = JsArray.of(JsStr.of("A"),
                                    TRUE,
                                    JsStr.of("B"),
                                    NULL,
                                    JsObj.of("a",
                                             JsStr.of("A"),
                                             "b",
                                             JsStr.of("B"),
                                             "c",
                                             JsArray.of(JsStr.of("A"),
                                                        JsStr.of("B"),
                                                        NULL
                                             )
                                    )
        );

        JsArray newArray1 = array1.mapValues((p, val) -> JsStr.prism.modify.apply(String::toLowerCase)
                                                                           .apply(val));
        Assertions.assertEquals(JsArray.parse("[\"a\",true,\"b\",null,{\"a\":\"a\",\"b\":\"b\",\"c\":[\"a\",\"b\",null]}]\n")
                ,
                                newArray1
        );

    }


    @Test
    public void test_map_values_obj() {


        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsStr.of("B")
        );

        JsObj newObj = obj.mapValues((p, val) -> JsStr.prism.modify.apply(String::toLowerCase)
                                                                   .apply(val));

        Assertions.assertNotEquals(obj,
                                   newObj
        );

        JsObj obj1 = JsObj.of("a",
                              JsStr.of("A"),
                              "b",
                              JsStr.of("B"),
                              "c",
                              JsArray.of(JsStr.of("A"),
                                         JsStr.of("B")
                              )
        );

        JsObj newObj1 = obj1.mapValues((p, val) -> JsStr.prism.modify.apply(String::toLowerCase)
                                                                     .apply(val));

        Assertions.assertNotEquals(obj1,
                                   newObj1
        );
    }

    @Test
    public void test_map_keys_obj() {

        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsStr.of("B")
        );

        JsObj newObj = obj.mapKeys(key -> key.toUpperCase());

        Assertions.assertNotEquals(newObj,
                                   obj
        );


        JsObj obj1 = JsObj.of("a",
                              JsStr.of("A"),
                              "b",
                              JsStr.of("B"),
                              "h",
                              JsArray.of(JsObj.of("c",
                                                  JsStr.of("C"),
                                                  "d",
                                                  JsStr.of("D")
                                         ),
                                         JsObj.of("d",
                                                  JsStr.of("D"),
                                                  "e",
                                                  JsStr.of("E")
                                         )
                              )
        );

        JsObj newObj1 = obj1.mapKeys((path, val) -> path.last()
                                                        .asKey().name.toUpperCase());

        Assertions.assertNotEquals(newObj1,
                                   obj1
        );


    }


    @Test
    public void test_readme_union() {
        JsObj a = JsObj.parse("{\"a\":1, \"c\": [{ \"d\":1 }] }");
        JsObj b = JsObj.parse("{\"b\":2, \"c\": [{ \"e\":2 }] }");
        JsObj c = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 }, { \"e\":2 }] }");
        JsObj d = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 , \"e\":2 }] }");

        Assertions.assertEquals(c,
                                a.union(b,
                                        SET
                                )
        );
        Assertions.assertEquals(d,
                                a.union(b,
                                        LIST
                                )
        );

        JsObj f = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} } ] }");
        JsObj g = JsObj.parse("{\"a\": [3, [4,5], 6, 7], \"b\": [1, 2] }");
        JsObj h = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 3, [4,5], 6, 7], \"b\":[1,2]}");
        JsObj i = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 7], \"b\":[1,2]}");

        Assertions.assertEquals(h,
                                f.union(g,
                                        SET
                                )
        );
        Assertions.assertEquals(h,
                                f.union(g,
                                        MULTISET
                                )
        );
        Assertions.assertEquals(i,
                                f.union(g,
                                        LIST
                                )
        );


    }

    @Test
    public void test_readme_intersection() {

        JsObj a = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1,2]}, {\"b\":2}, {\"c\":3}] } }");
        JsObj b = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1]  }, {\"b\":2}] } }");
        JsObj c = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"b\":2}] } }");


        Assertions.assertEquals(b,
                                a.intersection(b,
                                               LIST
                                )
        );



        Assertions.assertEquals(c,
                                a.intersection(b,
                                               MULTISET
                                )
        );
        JsObj d = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, true,  null, false    ] }");

        JsObj e = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, false, true, null, 1 ] }");

        JsObj i = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1}] }");




        Assertions.assertEquals(i,
                                d.intersection(e,
                                               LIST
                                )
        );


    }


}


