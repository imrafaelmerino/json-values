package jsonvalues;

import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

public class TestsUnionAndIntersection
{

    @Test
    public void test_1_testUnion() throws MalformedJson
    {


        JsObj a = Jsons.immutable.object.parse("{\"a\": 1, \"c\": [{\"d\": 1}]}")
                                        .orElseThrow();

        JsObj b = Jsons.immutable.object.parse("{\"b\": 2, \"c\": [{\"e\": 2}]}")
                                        .orElseThrow();

        JsObj c = Jsons.immutable.object.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1}, {\"e\": 2}]}")
                                        .orElseThrow();

        JsObj d = Jsons.immutable.object.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1,\"e\": 2}]}")
                                        .orElseThrow();

        JsObj e = Jsons.immutable.object.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1}]}")
                                        .orElseThrow();


        Assertions.assertEquals(c,
                                a.union_(b,
                                         SET
                                        )
                               );

        Assertions.assertEquals(c,
                                a.union_(b,
                                         MULTISET
                                        )
                               );

        Assertions.assertEquals(d,
                                a.union_(b,
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


        JsObj a = Jsons.immutable.object.parse("{\"a\": [1, 2]}")
                                        .orElseThrow();

        JsObj b = Jsons.immutable.object.parse("{\"a\": [3, [4, 5], 6]}")
                                        .orElseThrow();

        JsObj c = Jsons.immutable.object.parse("{\"a\":[1,2,3,[4,5],6]}")
                                        .orElseThrow();

        JsObj d = Jsons.immutable.object.parse("{\"a\":[1,2,6]}")
                                        .orElseThrow();


        Assertions.assertEquals(c,
                                a.union_(b,
                                         SET
                                        )
                               );

        Assertions.assertEquals(c,
                                a.union_(b,
                                         MULTISET
                                        )
                               );

        Assertions.assertEquals(d,
                                a.union_(b,
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


        JsArray a = Jsons.immutable.array.parse("[\"1\", \"2\"]")
                                         .orElseThrow();

        JsArray b = Jsons.immutable.array.parse("[\"3\", [\"4\", \"5\"], \"6\"]")
                                         .orElseThrow();

        JsArray c = Jsons.immutable.array.parse("[\"1\",\"2\",\"3\",[\"4\",\"5\"],\"6\"]")
                                         .orElseThrow();

        JsArray d = Jsons.immutable.array.parse("[\"1\",\"2\",\"6\"]")
                                         .orElseThrow();

        JsArray e = Jsons.immutable.array.parse("[\"1\",\"2\",\"3\", [\"4\", \"5\"], \"6\"]")
                                         .orElseThrow();

        JsArray f = Jsons.immutable.array.parse("[\"1\",\"2\",\"3\", [\"4\", \"5\"], \"6\",\"1\",\"2\",\"6\"]")
                                         .orElseThrow();


        Assertions.assertEquals(d,
                                a.union_(b
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


        JsObj a = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}]}")
                                        .orElseThrow();

        JsObj b = Jsons.immutable.object.parse("{\"a\": [3, [4, 5], 6, 7], \"b\": [1, 2]}")
                                        .orElseThrow();

        JsObj c = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 3, [4, 5], 6, 7], \"b\": [1, 2]}")
                                        .orElseThrow();

        JsObj d = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 7],\"b\": [1, 2]}")
                                        .orElseThrow();

        JsObj e = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}], \"b\": [1, 2]}")
                                        .orElseThrow();


        Assertions.assertEquals(c,
                                a.union_(b,
                                         SET
                                        )
                               );

        Assertions.assertEquals(c,
                                a.union_(b,
                                         MULTISET
                                        )
                               );

        Assertions.assertEquals(d,
                                a.union_(b,
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


        JsObj a = Jsons.immutable.object.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]} ]}")
                                        .orElseThrow();

        JsObj b = Jsons.immutable.object.parse("{\"a\":\"b\", \"b\":[4,5,6], \"c\":[ {\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}")
                                        .orElseThrow();

        JsObj c = Jsons.immutable.object.parse("{\"a\":1, \"b\":[1,2,3,4,5,6], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]}, "
                                               + "{\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}")
                                        .orElseThrow();

        JsObj d = Jsons.immutable.object.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2,5]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\",\"g\"]}]}")
                                        .orElseThrow();


        Assertions.assertEquals(c,
                                a.union_(b,
                                         SET
                                        )
                               );

        Assertions.assertEquals(c,
                                a.union_(b,
                                         MULTISET
                                        )
                               );

        Assertions.assertEquals(d,
                                a.union_(b,
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


        JsObj a = Jsons.immutable.object.parse("{\"a\":1,\"b\":2}")
                                        .orElseThrow();

        JsObj b = Jsons.immutable.object.parse("{\"a\":1}")
                                        .orElseThrow();

        JsObj c = Jsons.immutable.object.parse("{\"a\":1}")
                                        .orElseThrow();


        Assertions.assertEquals(c,
                                a.intersection(b,
                                               LIST
                                              )
                               );


    }

    @Test
    public void test_2_testIntersection() throws MalformedJson
    {


        JsObj a = Jsons.immutable.object.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1,2]},{\"b\":2},{\"c\":3}]}}")
                                        .orElseThrow();

        JsObj b = Jsons.immutable.object.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}")
                                        .orElseThrow();

        JsObj c = Jsons.immutable.object.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}")
                                        .orElseThrow();


        Assertions.assertEquals(c,
                                a.intersection_(b,
                                                LIST
                                               )
                               );


    }



    @Test
    public void test_map_values_immutable_array() throws MalformedJson
    {
        final Function<JsPair, JsElem> toLowerCaseFn = p -> p.mapIfStr(String::toLowerCase).elem;


        JsArray array = Jsons.immutable.array.of(JsStr.of("A"),
                                                 TRUE,
                                                 JsStr.of("B")
                                                );

        final JsArray newArray = array.mapElems(toLowerCaseFn,
                                                p -> p.elem.isStr()
                                               );

        Assertions.assertNotEquals(array,
                                   newArray
                                  );

        Assertions.assertEquals(Jsons.immutable.array.of(JsStr.of("a"),
                                                         TRUE,
                                                         JsStr.of("b")
                                                        ),
                                newArray
                               );

        JsArray array1 = Jsons.immutable.array.of(JsStr.of("A"),
                                                  TRUE,
                                                  JsStr.of("B"),
                                                  NULL,
                                                  Jsons.immutable.object.of("a",
                                                                            JsStr.of("A"),
                                                                            "b",
                                                                            JsStr.of("B"),
                                                                            "c",
                                                                            Jsons.immutable.array.of(JsStr.of("A"),
                                                                                                     JsStr.of("B"),
                                                                                                     NULL
                                                                                                    )
                                                                           )
                                                 );

        final JsArray newArray1 = array1.mapElems_(toLowerCaseFn,
                                                   p -> p.elem.isStr()
                                                  );
        Assertions.assertEquals(Jsons.immutable.array.parse("[\"a\",true,\"b\",null,{\"a\":\"a\",\"b\":\"b\",\"c\":[\"a\",\"b\",null]}]\n")
                                                     .orElseThrow(),
                                newArray1
                               );

    }



    @Test
    public void test_map_values_immutable_obj()
    {
        final Function<JsPair, JsElem> toLowerCaseFn = p -> p.mapIfStr(String::toLowerCase).elem;

        JsObj obj = Jsons.immutable.object.of("a",
                                              JsStr.of("A"),
                                              "b",
                                              JsStr.of("B")
                                             );

        final JsObj newObj = obj.mapElems(toLowerCaseFn);

        Assertions.assertNotEquals(obj,
                                   newObj
                                  );

        JsObj obj1 = Jsons.immutable.object.of("a",
                                               JsStr.of("A"),
                                               "b",
                                               JsStr.of("B"),
                                               "c",
                                               Jsons.immutable.array.of(JsStr.of("A"),
                                                                        JsStr.of("B")
                                                                       )
                                              );

        final JsObj newObj1 = obj1.mapElems_(toLowerCaseFn);

        Assertions.assertNotEquals(obj1,
                                   newObj1
                                  );
    }

    @Test
    public void test_map_keys_immutable_obj()
    {

        JsObj obj = Jsons.immutable.object.of("a",
                                              JsStr.of("A"),
                                              "b",
                                              JsStr.of("B")
                                             );

        final JsObj newObj = obj.mapKeys(p -> p.path.last()
                                                    .asKey().name.toUpperCase());

        Assertions.assertNotEquals(newObj,
                                   obj
                                  );


        JsObj obj1 = Jsons.immutable.object.of("a",
                                               JsStr.of("A"),
                                               "b",
                                               JsStr.of("B"),
                                               "h",
                                               Jsons.immutable.array.of(Jsons.immutable.object.of("c",
                                                                                                  JsStr.of("C"),
                                                                                                  "d",
                                                                                                  JsStr.of("D")
                                                                                                 ),
                                                                        Jsons.immutable.object.of("d",
                                                                                                  JsStr.of("D"),
                                                                                                  "e",
                                                                                                  JsStr.of("E")
                                                                                                 )
                                                                       )
                                              );

        final JsObj newObj1 = obj1.mapKeys_(p -> p.path.last()
                                                       .asKey().name.toUpperCase());

        Assertions.assertNotEquals(newObj1,
                                   obj1
                                  );


    }



    @Test
    public void test_map_keys_immutable_array() throws MalformedJson
    {

        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsStr.of("B"),
                                                                         "c",
                                                                         Jsons.immutable.object.empty()
                                                                        ),
                                               NULL,
                                               Jsons.immutable.object.of("c",
                                                                         JsInt.of(1),
                                                                         "d",
                                                                         JsStr.of("D")
                                                                        ),
                                               TRUE,
                                               Jsons.immutable.array.of(Jsons.immutable.object.of("e",
                                                                                                  JsInt.of(1),
                                                                                                  "f",
                                                                                                  JsStr.of("F")
                                                                                                 ))
                                              );

        Assertions.assertSame(arr,
                              arr.mapKeys(p -> p.path.last()
                                                     .asKey().name.toUpperCase())
                             );


        final JsArray arr1 = arr.mapKeys_(p -> p.path.last()
                                                     .asKey().name.toUpperCase(),
                                          p -> p.elem.isStr()
                                         );


        Assertions.assertNotEquals(arr1,
                                   arr
                                  );

        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"a\":1,\"B\":\"B\",\"c\":{}},null,{\"c\":1,\"D\":\"D\"},true,[{\"e\":1,\"F\":\"F\"}]]\n")
                                                     .orElseThrow(),
                                arr1
                               );

        final JsArray arr2 = arr.mapKeys_(p -> p.path.last()
                                                     .asKey().name.toUpperCase()
                                         );

        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"A\":1,\"B\":\"B\",\"C\":{}},null,{\"C\":1,\"D\":\"D\"},true,[{\"E\":1,\"F\":\"F\"}]]\n")
                                                     .orElseThrow(),
                                arr2
                               );


    }


    @Test
    public void test_readme_union() throws MalformedJson
    {
        JsObj a = Jsons.immutable.object.parse("{\"a\":1, \"c\": [{ \"d\":1 }] }")
                                        .orElseThrow();
        JsObj b = Jsons.immutable.object.parse("{\"b\":2, \"c\": [{ \"e\":2 }] }")
                                        .orElseThrow();
        JsObj c = Jsons.immutable.object.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 }, { \"e\":2 }] }")
                                        .orElseThrow();
        JsObj d = Jsons.immutable.object.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 , \"e\":2 }] }")
                                        .orElseThrow();
        JsObj e = Jsons.immutable.object.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1  }] }")
                                        .orElseThrow();

        Assertions.assertEquals(c,
                                a.union_(b,
                                         SET
                                        )
                               );
        Assertions.assertEquals(d,
                                a.union_(b,
                                         LIST
                                        )
                               );
        Assertions.assertEquals(e,
                                a.union(b)
                               );

        JsObj f = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} } ] }")
                                        .orElseThrow();
        JsObj g = Jsons.immutable.object.parse("{\"a\": [3, [4,5], 6, 7], \"b\": [1, 2] }")
                                        .orElseThrow();
        JsObj h = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 3, [4,5], 6, 7], \"b\":[1,2]}")
                                        .orElseThrow();
        JsObj i = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 7], \"b\":[1,2]}")
                                        .orElseThrow();
        JsObj j = Jsons.immutable.object.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }], \"b\":[1,2]}")
                                        .orElseThrow();


        Assertions.assertEquals(h,
                                f.union_(g,
                                         SET
                                        )
                               );
        Assertions.assertEquals(h,
                                f.union_(g,
                                         MULTISET
                                        )
                               );
        Assertions.assertEquals(i,
                                f.union_(g,
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

        JsObj a = Jsons.immutable.object.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1,2]}, {\"b\":2}, {\"c\":3}] } }")
                                        .orElseThrow();
        JsObj b = Jsons.immutable.object.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1]  }, {\"b\":2}] } }")
                                        .orElseThrow();
        JsObj c = Jsons.immutable.object.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"b\":2}] } }")
                                        .orElseThrow();

        Assertions.assertEquals(Jsons.immutable.object.empty(),
                                a.intersection(b,
                                               LIST
                                              )
                               );
        Assertions.assertEquals(b,
                                a.intersection_(b,
                                                LIST
                                               )
                               );
        Assertions.assertEquals(Jsons.immutable.object.empty(),
                                a.intersection(b,
                                               SET
                                              )
                               );
        Assertions.assertEquals(c,
                                a.intersection_(b,
                                                SET
                                               )
                               );
        Assertions.assertEquals(Jsons.immutable.object.empty(),
                                a.intersection(b,
                                               MULTISET
                                              )
                               );
        Assertions.assertEquals(c,
                                a.intersection_(b,
                                                MULTISET
                                               )
                               );
        JsObj d = Jsons.immutable.object.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, true,  null, false    ] }")
                                        .orElseThrow();

        JsObj e = Jsons.immutable.object.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, false, true, null, 1 ] }")
                                        .orElseThrow();
        JsObj f = Jsons.immutable.object.parse("{ \"a\": true }")
                                        .orElseThrow();

        JsObj i = Jsons.immutable.object.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1}] }")
                                        .orElseThrow();

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
                                d.intersection_(e,
                                                LIST
                                               )
                               );


    }
}


