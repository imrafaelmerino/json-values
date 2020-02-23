package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

public class TestsUnionAndIntersection
{

    @Test
    public void test_1_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\": 1, \"c\": [{\"d\": 1}]}")
                                        ;

        JsObj b = JsObj.parse("{\"b\": 2, \"c\": [{\"e\": 2}]}")
                                        ;

        JsObj c = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1}, {\"e\": 2}]}")
                                        ;

        JsObj d = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1,\"e\": 2}]}")
                                        ;

        JsObj e = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1}]}")
                                        ;


        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           SET
                                          )
                               );

        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           MULTISET
                                          )
                               );

        Assertions.assertEquals(d,
                                a.unionAll(b,
                                           LIST
                                          )
                               );

        Assertions.assertEquals(e,
                                a.union(b)
                               );

    }

    @Test
    public void test_2_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\": [1, 2]}")
                                        ;

        JsObj b = JsObj.parse("{\"a\": [3, [4, 5], 6]}")
                                        ;

        JsObj c = JsObj.parse("{\"a\":[1,2,3,[4,5],6]}")
                                        ;

        JsObj d = JsObj.parse("{\"a\":[1,2,6]}")
                                        ;


        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           SET
                                          )
                               );

        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           MULTISET
                                          )
                               );

        Assertions.assertEquals(d,
                                a.unionAll(b,
                                           LIST
                                          )
                               );

        Assertions.assertEquals(a,
                                a.union(b)
                               );

    }

    @Test
    public void test_3_testUnion() throws MalformedJson
    {


        JsArray a = JsArray.parse("[\"1\", \"2\"]")
                                         ;

        JsArray b = JsArray.parse("[\"3\", [\"4\", \"5\"], \"6\"]")
                                         ;

        JsArray c = JsArray.parse("[\"1\",\"2\",\"3\",[\"4\",\"5\"],\"6\"]")
                                         ;

        JsArray d = JsArray.parse("[\"1\",\"2\",\"6\"]")
                                         ;

        JsArray e = JsArray.parse("[\"1\",\"2\",\"3\", [\"4\", \"5\"], \"6\"]")
                                         ;

        JsArray f = JsArray.parse("[\"1\",\"2\",\"3\", [\"4\", \"5\"], \"6\",\"1\",\"2\",\"6\"]")
                                         ;


        Assertions.assertEquals(d,
                                a.unionAll(b
                                          )
                               );

        Assertions.assertEquals(d,
                                a.union(b,
                                        LIST
                                       )
                               );

        Assertions.assertEquals(e,
                                a.union(b,
                                        SET
                                       )
                               );

        Assertions.assertEquals(f,
                                c.union(d,
                                        MULTISET
                                       )
                               );

    }

    @Test
    public void test_4_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}]}")
                                        ;

        JsObj b = JsObj.parse("{\"a\": [3, [4, 5], 6, 7], \"b\": [1, 2]}")
                                        ;

        JsObj c = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 3, [4, 5], 6, 7], \"b\": [1, 2]}")
                                        ;

        JsObj d = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 7],\"b\": [1, 2]}")
                                        ;

        JsObj e = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}], \"b\": [1, 2]}")
                                        ;


        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           SET
                                          )
                               );

        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           MULTISET
                                          )
                               );

        Assertions.assertEquals(d,
                                a.unionAll(b,
                                           LIST
                                          )
                               );

        Assertions.assertEquals(e,
                                a.union(b)
                               );

    }

    @Test
    public void test_5_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]} ]}")
                                        ;

        JsObj b = JsObj.parse("{\"a\":\"b\", \"b\":[4,5,6], \"c\":[ {\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}")
                                        ;

        JsObj c = JsObj.parse("{\"a\":1, \"b\":[1,2,3,4,5,6], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]}, "
                                               + "{\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}")
                                        ;

        JsObj d = JsObj.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2,5]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\",\"g\"]}]}")
                                        ;


        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           SET
                                          )
                               );

        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           MULTISET
                                          )
                               );

        Assertions.assertEquals(d,
                                a.unionAll(b,
                                           LIST
                                          )
                               );

        Assertions.assertEquals(a,
                                a.union(b)
                               );


    }

    @Test
    public void test_1_testIntersection() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\":1,\"b\":2}")
                                        ;

        JsObj b = JsObj.parse("{\"a\":1}")
                                        ;

        JsObj c = JsObj.parse("{\"a\":1}")
                                        ;


        Assertions.assertEquals(c,
                                a.intersection(b,
                                               LIST
                                              )
                               );


    }

    @Test
    public void test_2_testIntersection() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1,2]},{\"b\":2},{\"c\":3}]}}")
                                        ;

        JsObj b = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}")
                                        ;

        JsObj c = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}")
                                        ;


        Assertions.assertEquals(c,
                                a.intersectionAll(b,
                                                  LIST
                                                 )
                               );


    }



    @Test
    public void test_map_values_immutable_array() throws MalformedJson
    {
        final Function<JsPair, JsValue> toLowerCaseFn = p -> p.mapIfStr(String::toLowerCase).value;


        JsArray array = JsArray.of(JsStr.of("A"),
                                                 TRUE,
                                                 JsStr.of("B")
                                                );

        final JsArray newArray = array.mapValues(toLowerCaseFn,
                                                p -> p.value.isStr()
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

        final JsArray newArray1 = array1.mapAllValues(toLowerCaseFn,
                                                   p -> p.value.isStr()
                                                     );
        Assertions.assertEquals(JsArray.parse("[\"a\",true,\"b\",null,{\"a\":\"a\",\"b\":\"b\",\"c\":[\"a\",\"b\",null]}]\n")
                                                     ,
                                newArray1
                               );

    }



    @Test
    public void test_map_values_immutable_obj()
    {
        final Function<JsPair, JsValue> toLowerCaseFn = p -> p.mapIfStr(String::toLowerCase).value;

        JsObj obj = JsObj.of("a",
                                              JsStr.of("A"),
                                              "b",
                                              JsStr.of("B")
                                             );

        final JsObj newObj = obj.mapValues(toLowerCaseFn);

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

        final JsObj newObj1 = obj1.mapAllValues(toLowerCaseFn);

        Assertions.assertNotEquals(obj1,
                                   newObj1
                                  );
    }

    @Test
    public void test_map_keys_immutable_obj()
    {

        JsObj obj = JsObj.of("a",
                                              JsStr.of("A"),
                                              "b",
                                              JsStr.of("B")
                                             );

        final JsObj newObj = obj.mapKeys(p -> p.path.last()
                                                    .asKey().name.toUpperCase());

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

        final JsObj newObj1 = obj1.mapAllKeys(p -> p.path.last()
                                                         .asKey().name.toUpperCase());

        Assertions.assertNotEquals(newObj1,
                                   obj1
                                  );


    }



    @Test
    public void test_map_keys_immutable_array() throws MalformedJson
    {

        JsArray arr = JsArray.of(JsObj.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsStr.of("B"),
                                                                         "c",
                                                                         JsObj.empty()
                                                                        ),
                                               NULL,
                                               JsObj.of("c",
                                                                         JsInt.of(1),
                                                                         "d",
                                                                         JsStr.of("D")
                                                                        ),
                                               TRUE,
                                               JsArray.of(JsObj.of("e",
                                                                                                  JsInt.of(1),
                                                                                                  "f",
                                                                                                  JsStr.of("F")
                                                                                                 ))
                                              );

        Assertions.assertSame(arr,
                              arr.mapKeys(p -> p.path.last()
                                                     .asKey().name.toUpperCase())
                             );


        final JsArray arr1 = arr.mapAllKeys(p -> p.path.last()
                                                       .asKey().name.toUpperCase(),
                                          p -> p.value.isStr()
                                           );


        Assertions.assertNotEquals(arr1,
                                   arr
                                  );

        Assertions.assertEquals(JsArray.parse("[{\"a\":1,\"B\":\"B\",\"c\":{}},null,{\"c\":1,\"D\":\"D\"},true,[{\"e\":1,\"F\":\"F\"}]]\n")
                                                     ,
                                arr1
                               );

        final JsArray arr2 = arr.mapAllKeys(p -> p.path.last()
                                                       .asKey().name.toUpperCase()
                                           );

        Assertions.assertEquals(JsArray.parse("[{\"A\":1,\"B\":\"B\",\"C\":{}},null,{\"C\":1,\"D\":\"D\"},true,[{\"E\":1,\"F\":\"F\"}]]\n")
                                                     ,
                                arr2
                               );


    }


    @Test
    public void test_readme_union() throws MalformedJson
    {
        JsObj a = JsObj.parse("{\"a\":1, \"c\": [{ \"d\":1 }] }")
                                        ;
        JsObj b = JsObj.parse("{\"b\":2, \"c\": [{ \"e\":2 }] }")
                                        ;
        JsObj c = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 }, { \"e\":2 }] }")
                                        ;
        JsObj d = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 , \"e\":2 }] }")
                                        ;
        JsObj e = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1  }] }")
                                        ;

        Assertions.assertEquals(c,
                                a.unionAll(b,
                                           SET
                                          )
                               );
        Assertions.assertEquals(d,
                                a.unionAll(b,
                                           LIST
                                          )
                               );
        Assertions.assertEquals(e,
                                a.union(b)
                               );

        JsObj f = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} } ] }")
                                        ;
        JsObj g = JsObj.parse("{\"a\": [3, [4,5], 6, 7], \"b\": [1, 2] }")
                                        ;
        JsObj h = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 3, [4,5], 6, 7], \"b\":[1,2]}")
                                        ;
        JsObj i = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 7], \"b\":[1,2]}")
                                        ;
        JsObj j = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }], \"b\":[1,2]}")
                                        ;


        Assertions.assertEquals(h,
                                f.unionAll(g,
                                           SET
                                          )
                               );
        Assertions.assertEquals(h,
                                f.unionAll(g,
                                           MULTISET
                                          )
                               );
        Assertions.assertEquals(i,
                                f.unionAll(g,
                                           LIST
                                          )
                               );
        Assertions.assertEquals(j,
                                f.union(g)
                               );

    }

    @Test
    public void test_readme_intersection() throws MalformedJson
    {

        JsObj a = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1,2]}, {\"b\":2}, {\"c\":3}] } }")
                                        ;
        JsObj b = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1]  }, {\"b\":2}] } }")
                                        ;
        JsObj c = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"b\":2}] } }")
                                        ;

        Assertions.assertEquals(JsObj.empty(),
                                a.intersection(b,
                                               LIST
                                              )
                               );
        Assertions.assertEquals(b,
                                a.intersectionAll(b,
                                                  LIST
                                                 )
                               );
        Assertions.assertEquals(JsObj.empty(),
                                a.intersection(b,
                                               SET
                                              )
                               );
        Assertions.assertEquals(c,
                                a.intersectionAll(b,
                                                  SET
                                                 )
                               );
        Assertions.assertEquals(JsObj.empty(),
                                a.intersection(b,
                                               MULTISET
                                              )
                               );
        Assertions.assertEquals(c,
                                a.intersectionAll(b,
                                                  MULTISET
                                                 )
                               );
        JsObj d = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, true,  null, false    ] }")
                                        ;

        JsObj e = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, false, true, null, 1 ] }")
                                        ;
        JsObj f = JsObj.parse("{ \"a\": true }")
                                        ;

        JsObj i = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1}] }")
                                        ;

        Assertions.assertEquals(d,
                                d.intersection(e,
                                               SET
                                              )
                               );

        Assertions.assertEquals(f,
                                d.intersection(e,
                                               MULTISET
                                              )
                               );
        Assertions.assertEquals(f,
                                d.intersection(e,
                                               LIST
                                              )
                               );
        Assertions.assertEquals(i,
                                d.intersectionAll(e,
                                                  LIST
                                                 )
                               );


    }
}


