import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

class TestsUnionAndIntersection
{

    @Test
    void _1_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\": 1, \"c\": [{\"d\": 1}]}")
                       .orElseThrow();

        JsObj b = JsObj.parse("{\"b\": 2, \"c\": [{\"e\": 2}]}")
                       .orElseThrow();

        JsObj c = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1}, {\"e\": 2}]}")
                       .orElseThrow();

        JsObj d = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1,\"e\": 2}]}")
                       .orElseThrow();

        JsObj e = JsObj.parse("{\"a\": 1, \"b\": 2, \"c\": [{\"d\": 1}]}")
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
    void _2_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\": [1, 2]}")
                       .orElseThrow();

        JsObj b = JsObj.parse("{\"a\": [3, [4, 5], 6]}")
                       .orElseThrow();

        JsObj c = JsObj.parse("{\"a\":[1,2,3,[4,5],6]}")
                       .orElseThrow();

        JsObj d = JsObj.parse("{\"a\":[1,2,6]}")
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
    void _3_testUnion() throws MalformedJson
    {


        JsArray a = JsArray.parse("[\"1\", \"2\"]")
                           .orElseThrow();

        JsArray b = JsArray.parse("[\"3\", [\"4\", \"5\"], \"6\"]")
                           .orElseThrow();

        JsArray c = JsArray.parse("[\"1\",\"2\",\"3\",[\"4\",\"5\"],\"6\"]")
                           .orElseThrow();

        JsArray d = JsArray.parse("[\"1\",\"2\",\"6\"]")
                           .orElseThrow();

        JsArray e = JsArray.parse("[\"1\",\"2\",\"3\", [\"4\", \"5\"], \"6\"]")
                           .orElseThrow();

        JsArray f = JsArray.parse("[\"1\",\"2\",\"3\", [\"4\", \"5\"], \"6\",\"1\",\"2\",\"6\"]")
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
    void _4_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}]}")
                       .orElseThrow();

        JsObj b = JsObj.parse("{\"a\": [3, [4, 5], 6, 7], \"b\": [1, 2]}")
                       .orElseThrow();

        JsObj c = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 3, [4, 5], 6, 7], \"b\": [1, 2]}")
                       .orElseThrow();

        JsObj d = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}, 7],\"b\": [1, 2]}")
                       .orElseThrow();

        JsObj e = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\": 1}}], \"b\": [1, 2]}")
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
    void _5_testUnion() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]} ]}")
                       .orElseThrow();

        JsObj b = JsObj.parse("{\"a\":\"b\", \"b\":[4,5,6], \"c\":[ {\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}")
                       .orElseThrow();

        JsObj c = JsObj.parse("{\"a\":1, \"b\":[1,2,3,4,5,6], \"c\":[ {\"d\":1,\"e\":[1,2]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\"]}, "
                              + "{\"d\":\"a\",\"e\":[3,4,5]}, {\"f\":2,\"g\":[\"d\",\"e\",\"f\",\"g\"]}]}")
                       .orElseThrow();

        JsObj d = JsObj.parse("{\"a\":1, \"b\":[1,2,3], \"c\":[ {\"d\":1,\"e\":[1,2,5]}, {\"f\":2,\"g\":[\"a\",\"b\",\"c\",\"g\"]}]}")
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
    void _1_testIntersection() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"a\":1,\"b\":2}")
                       .orElseThrow();

        JsObj b = JsObj.parse("{\"a\":1}")
                       .orElseThrow();

        JsObj c = JsObj.parse("{\"a\":1}")
                       .orElseThrow();


        Assertions.assertEquals(c,
                                a.intersection(b,
                                               LIST
                                              )
                               );


    }


    @Test
    void _2_testIntersection() throws MalformedJson
    {


        JsObj a = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1,2]},{\"b\":2},{\"c\":3}]}}")
                       .orElseThrow();

        JsObj b = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}")
                       .orElseThrow();

        JsObj c = JsObj.parse("{\"b\":{\"a\":1,\"b\":2,\"c\":[{\"a\":1,\"b\":[1]}]}}")
                       .orElseThrow();


        Assertions.assertEquals(c,
                                a.intersection_(b,
                                                LIST
                                               )
                               );


    }

    @Test
    void test_map_values_mutable_array()
    {

        final Function<JsPair, JsElem> toLowerCaseFn = Utils.mapIfStr(String::toLowerCase)
                                                            .andThen(p -> p.elem);
        JsArray array = JsArray._of_(JsStr.of("A"),
                                     JsStr.of("B")
                                    );

        Assertions.assertEquals(array,
                                array.mapElems(toLowerCaseFn)
                               );

        JsArray array1 = JsArray._of_(JsStr.of("A"),
                                      JsStr.of("B"),
                                      JsObj._of_("a",
                                                 JsStr.of("A"),
                                                 "b",
                                                 JsStr.of("B")
                                                )
                                     );

        Assertions.assertEquals(array1,
                                array1.mapElems_(toLowerCaseFn)
                               );


    }

    @Test
    void test_map_values_immutable_array() throws MalformedJson
    {
        final Function<JsPair, JsElem> toLowerCaseFn = Utils.mapIfStr(String::toLowerCase)
                                                            .andThen(p -> p.elem);


        JsArray array = JsArray.of(JsStr.of("A"),
                                   TRUE,
                                   JsStr.of("B")
                                  );

        final JsArray newArray = array.mapElems(toLowerCaseFn,
                                                p -> p.elem.isStr()
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

        final JsArray newArray1 = array1.mapElems_(toLowerCaseFn,
                                                   p -> p.elem.isStr()
                                                  );
        Assertions.assertEquals(JsArray.parse("[\"a\",true,\"b\",null,{\"a\":\"a\",\"b\":\"b\",\"c\":[\"a\",\"b\",null]}]\n")
                                       .orElseThrow(),
                                newArray1
                               );

    }

    @Test
    void test_map_values_mutable_obj()
    {
        final Function<JsPair, JsElem> toLowerCaseFn = Utils.mapIfStr(String::toLowerCase)
                                                            .andThen(p -> p.elem);

        JsObj obj = JsObj._of_("a",
                               JsStr.of("A"),
                               "b",
                               JsStr.of("B")
                              );

        final JsObj newObj = obj.mapElems(toLowerCaseFn);

        Assertions.assertEquals(obj,
                                newObj
                               );

        JsObj obj1 = JsObj._of_("a",
                                JsStr.of("A"),
                                "b",
                                JsStr.of("B"),
                                "c",
                                JsArray._of_(JsStr.of("A"),
                                             JsStr.of("B")
                                            )
                               );

        final JsObj newObj1 = obj1.mapElems_(toLowerCaseFn);

        Assertions.assertEquals(obj1,
                                newObj1
                               );
    }

    @Test
    void test_map_values_immutable_obj()
    {
        final Function<JsPair, JsElem> toLowerCaseFn = Utils.mapIfStr(String::toLowerCase)
                                                            .andThen(p -> p.elem);

        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsStr.of("B")
                            );

        final JsObj newObj = obj.mapElems(toLowerCaseFn);

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

        final JsObj newObj1 = obj1.mapElems_(toLowerCaseFn);

        Assertions.assertNotEquals(obj1,
                                   newObj1
                                  );
    }

    @Test
    void map_keys_immutable_obj()
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

        final JsObj newObj1 = obj1.mapKeys_(p -> p.path.last()
                                                       .asKey().name.toUpperCase());

        Assertions.assertNotEquals(newObj1,
                                   obj1
                                  );


    }

    @Test
    void map_keys_mutable_obj()
    {

        JsObj obj = JsObj._of_("a",
                               JsStr.of("A"),
                               "b",
                               JsStr.of("B")
                              );

        final JsObj newObj = obj.mapKeys(p -> p.path.last()
                                                    .asKey().name.toUpperCase());

        Assertions.assertEquals(newObj,
                                obj
                               );


        JsObj obj1 = JsObj._of_("a",
                                JsStr.of("A"),
                                "b",
                                JsStr.of("B"),
                                "h",
                                JsArray._of_(JsObj._of_("c",
                                                        JsStr.of("C"),
                                                        "d",
                                                        JsStr.of("D")
                                                       ),
                                             JsObj._of_("d",
                                                        JsStr.of("D"),
                                                        "e",
                                                        JsStr.of("E")
                                                       )
                                            )
                               );

        final JsObj newObj1 = obj1.mapKeys_(p -> p.path.last()
                                                       .asKey().name.toUpperCase());

        Assertions.assertEquals(newObj1,
                                obj1
                               );


    }

    @Test
    void map_keys_immutable_array() throws MalformedJson
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


        final JsArray arr1 = arr.mapKeys_(p -> p.path.last()
                                                     .asKey().name.toUpperCase(),
                                          p -> p.elem.isStr()
                                         );


        Assertions.assertNotEquals(arr1,
                                   arr
                                  );

        Assertions.assertEquals(JsArray.parse("[{\"a\":1,\"B\":\"B\",\"c\":{}},null,{\"c\":1,\"D\":\"D\"},true,[{\"e\":1,\"F\":\"F\"}]]\n")
                                       .orElseThrow(),
                                arr1
                               );

        final JsArray arr2 = arr.mapKeys_(p -> p.path.last()
                                                     .asKey().name.toUpperCase()
                                         );

        Assertions.assertEquals(JsArray.parse("[{\"A\":1,\"B\":\"B\",\"C\":{}},null,{\"C\":1,\"D\":\"D\"},true,[{\"E\":1,\"F\":\"F\"}]]\n")
                                       .orElseThrow(),
                                arr2
                               );


    }

    @Test
    void map_keys_mutable_array() throws MalformedJson
    {
        Supplier<JsArray> supplier = () -> JsArray._of_(JsObj._of_("a",
                                                                   JsStr.of("A"),
                                                                   "b",
                                                                   JsStr.of("B")
                                                                  ),
                                                        JsObj._of_("c",
                                                                   JsStr.of("C"),
                                                                   "d",
                                                                   JsStr.of("D")
                                                                  ),
                                                        JsArray._of_(JsObj._of_("e",
                                                                                JsStr.of("E"),
                                                                                "f",
                                                                                JsStr.of("F")
                                                                               ))
                                                       );

        final JsArray arr = supplier.get();
        final Function<JsPair, String> toUpperCase = p -> p.path.last()
                                                                .asKey().name.toUpperCase();
        Assertions.assertSame(arr,
                              arr.mapKeys(toUpperCase)
                             );

        Assertions.assertSame(arr,
                              arr.mapKeys(toUpperCase,
                                          p -> false
                                         )
                             );

        final JsArray arr1 = supplier.get();
        arr1.mapKeys_(toUpperCase,
                      p -> !p.path.last()
                                  .asKey().name.equals("e")
                     );

        Assertions.assertEquals(JsArray._parse_("[{\"A\":\"A\",\"B\":\"B\"},{\"C\":\"C\",\"D\":\"D\"},[{\"e\":\"E\",\"F\":\"F\"}]]\n")
                                       .orElseThrow(),
                                arr1
                               );
        final JsArray arr2 = supplier.get();
        arr2.mapKeys_(toUpperCase
                     );
        Assertions.assertEquals(JsArray._parse_("[{\"A\":\"A\",\"B\":\"B\"},{\"C\":\"C\",\"D\":\"D\"},[{\"E\":\"E\",\"F\":\"F\"}]]\n")
                                       .orElseThrow(),
                                arr2
                               );
    }

    @Test
    void test_filter_empty_mutable_jsons()
    {

        Supplier<JsObj> supplier = () -> JsObj._of_("a",
                                                    JsStr.of("A"),
                                                    "b",
                                                    JsObj._empty_(),
                                                    "c",
                                                    JsStr.of("C"),
                                                    "d",
                                                    JsObj._of_("e",
                                                               JsArray._of_("A",
                                                                            "g",
                                                                            "h"
                                                                           )
                                                                      .prepend(JsObj._empty_())
                                                                      .prepend(JsObj._empty_()),
                                                               "b",
                                                               JsObj._empty_(),
                                                               "h",
                                                               JsObj._empty_(),
                                                               "i",
                                                               TRUE,
                                                               "j",
                                                               JsStr.of("A")
                                                              )
                                                   );


        final JsObj obj = supplier.get();
        final JsObj newObj = obj.filterObjs_((p, o) ->
                                             {
                                                 Assertions.assertEquals(o,
                                                                         supplier.get()
                                                                                 .get(p)
                                                                        );
                                                 return o.isNotEmpty();
                                             });

        Assertions.assertEquals(obj,
                                newObj
                               );

        Assertions.assertEquals(Optional.empty(),
                                obj.stream_()
                                   .filter(p -> p.elem.isObj(JsObj::isEmpty))
                                   .findFirst()
                               );


    }

    @Test
    void test_filter_values_mutable()
    {
        Supplier<JsObj> supplier = () -> JsObj._of_("a",
                                                    JsStr.of("A"),
                                                    "b",
                                                    JsObj._empty_(),
                                                    "c",
                                                    JsStr.of("C"),
                                                    "d",
                                                    JsObj._of_("e",
                                                               JsArray._of_("A",
                                                                            "g",
                                                                            "h"
                                                                           )
                                                                      .prepend(JsObj._empty_())
                                                                      .prepend(JsObj._empty_()),
                                                               "b",
                                                               JsObj._empty_(),
                                                               "h",
                                                               JsObj._empty_(),
                                                               "i",
                                                               TRUE,
                                                               "j",
                                                               JsStr.of("A")
                                                              )
                                                   );

        supplier.get()
                .filterElems_(p ->
                              {
                                  Assertions.assertEquals(p.elem,
                                                          supplier.get()
                                                                  .get(p.path)
                                                         );
                                  return p.elem.isStr(s -> s.equals("A"));
                              })
                .stream_()
                .filter(p -> p.elem.isNotJson())
                .forEach(p -> Assertions.assertTrue(p.elem.isStr(s -> s.equals("A"))));


        supplier.get()
                .filterObjs_((p, o) ->
                             {
                                 Assertions.assertEquals(o,
                                                         supplier.get()
                                                                 .get(p)
                                                        );
                                 return o.isNotEmpty();
                             })
                .stream_()
                .filter(p -> p.elem.isObj())
                .forEach(p -> Assertions.assertTrue(p.elem.asJsObj()
                                                          .isNotEmpty()));

    }

    @Test
    void test_readme_union() throws MalformedJson
    {
        JsObj a = JsObj.parse("{\"a\":1, \"c\": [{ \"d\":1 }] }")
                       .orElseThrow();
        JsObj b = JsObj.parse("{\"b\":2, \"c\": [{ \"e\":2 }] }")
                       .orElseThrow();
        JsObj c = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 }, { \"e\":2 }] }")
                       .orElseThrow();
        JsObj d = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1 , \"e\":2 }] }")
                       .orElseThrow();
        JsObj e = JsObj.parse("{\"a\":1, \"b\":2, \"c\": [{ \"d\":1  }] }")
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

        JsObj f = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} } ] }")
                       .orElseThrow();
        JsObj g = JsObj.parse("{\"a\": [3, [4,5], 6, 7], \"b\": [1, 2] }")
                       .orElseThrow();
        JsObj h = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 3, [4,5], 6, 7], \"b\":[1,2]}")
                       .orElseThrow();
        JsObj i = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }, 7], \"b\":[1,2]}")
                       .orElseThrow();
        JsObj j = JsObj.parse("{\"a\": [1, 2, {\"b\": {\"b\":1} }], \"b\":[1,2]}")
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
    void test_readme_intersection() throws MalformedJson
    {

        JsObj a = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1,2]}, {\"b\":2}, {\"c\":3}] } }")
                       .orElseThrow();
        JsObj b = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"a\":1, \"b\":[1]  }, {\"b\":2}] } }")
                       .orElseThrow();
        JsObj c = JsObj.parse("{ \"b\": {\"a\":1, \"b\":2, \"c\": [{\"b\":2}] } }")
                       .orElseThrow();

        Assertions.assertEquals(JsObj.empty(),
                                a.intersection(b,
                                               LIST
                                              )
                               );
        Assertions.assertEquals(b,
                                a.intersection_(b,
                                                LIST
                                               )
                               );
        Assertions.assertEquals(JsObj.empty(),
                                a.intersection(b,
                                               SET
                                              )
                               );
        Assertions.assertEquals(c,
                                a.intersection_(b,
                                                SET
                                               )
                               );
        Assertions.assertEquals(JsObj.empty(),
                                a.intersection(b,
                                               MULTISET
                                              )
                               );
        Assertions.assertEquals(c,
                                a.intersection_(b,
                                                MULTISET
                                               )
                               );
        JsObj d = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, true,  null, false    ] }")
                       .orElseThrow();

        JsObj e = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1         }, false, true, null, 1 ] }")
                       .orElseThrow();
        JsObj f = JsObj.parse("{ \"a\": true }")
                       .orElseThrow();

        JsObj i = JsObj.parse("{ \"a\":true, \"b\": [1, 2, {\"a\":1}] }")
                       .orElseThrow();

        Assertions.assertEquals(d,
                                d.intersection(e,
                                               SET));

        Assertions.assertEquals(f,
                                d.intersection(e,
                                               MULTISET));
        Assertions.assertEquals(f,
                                d.intersection(e,
                                               LIST));
        Assertions.assertEquals(i,
                                d.intersection_(e,
                                                LIST));


    }
}


