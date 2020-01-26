package jsonvalues;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsPath.path;

public class TestJsArray
{


    @Test
    public void test_creation_immutable_two_element_json_array()
    {
        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsInt.of(2),
                                               JsStr.of("a"),
                                               NULL,
                                               JsStr.of("h"),
                                               Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         NULL
                                                                        )
                                              );


        final int result = arr.mapElems(p -> p.mapIfInt(i -> i + 100).elem
                                       )
                              .reduce(Integer::sum,
                                      pair -> pair.elem.asJsInt().x,
                                      p -> p.elem.isInt()
                                     )
                              .orElse(-1);

        final int result1 = arr.mapElems(p -> p.elem.asJsInt()
                                                    .map(i -> i + 100),
                                         p -> p.elem.isInt()
                                        )
                               .reduce(Integer::sum,
                                       pair -> pair.elem.asJsInt().x,
                                       p -> p.elem.isInt()
                                      )
                               .orElse(-1);


        Assertions.assertEquals(203,
                                result
                               );

        Assertions.assertEquals(203,
                                result1
                               );


        final int result2 = arr.mapElems_(p -> p.mapIfInt(i -> i + 100).elem
                                         )
                               .reduce_(Integer::sum,
                                        pair -> pair.elem.asJsInt().x,
                                        p -> p.elem.isInt()
                                       )
                               .orElse(-1);

        final int result3 = arr.mapElems_(p -> p.elem.asJsInt()
                                                     .map(i -> i + 100),
                                          p -> p.elem.isInt()
                                         )
                               .reduce_(Integer::sum,
                                        pair -> pair.elem.asJsInt().x,
                                        p -> p.elem.isInt()
                                       )
                               .orElse(-1);
        Assertions.assertEquals(406,
                                result2

                               );
        Assertions.assertEquals(406,
                                result3
                               );
    }


    @Test
    public void test_prepend()
    {

        JsArray arr = Jsons.immutable.array.of("a",
                                               "b"
                                              );

        Assertions.assertEquals(Jsons.immutable.array.of("c",
                                                         "d",
                                                         "a",
                                                         "b"
                                                        ),
                                arr.prepend(JsStr.of("c"),
                                            JsStr.of("d")
                                           )
                               );

        Assertions.assertEquals(2,
                                arr.size()
                               );


        JsArray arr3 = Jsons.immutable.array.empty()
                                            .prepend(path("/2/1"),
                                                     Jsons.immutable.array.of("a",
                                                                              "b",
                                                                              "c"
                                                                             )
                                                    );

        Assertions.assertEquals(NULL,
                                arr3.get(path("/0"))
                               );
        Assertions.assertEquals(NULL,
                                arr3.get(path("/1"))
                               );
        Assertions.assertEquals(NULL,
                                arr3.get(path("/2/0"))
                               );

        JsArray arr4 = arr3.append(path("/2/1"),
                                   TRUE
                                  )
                           .prepend(path("/2/1"),
                                    TRUE
                                   );

        Assertions.assertEquals(TRUE,
                                arr4.get(path("/2/1/-1"))
                               );
        Assertions.assertEquals(TRUE,
                                arr4.get(path("/2/1/0"))
                               );


    }


    @Test
    public void test_contains_element_in_js_array()
    {
        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );
        Assertions.assertTrue(arr.containsElem(JsInt.of(1)));   //true
        Assertions.assertTrue(arr.containsElem(TRUE));   //true
        Assertions.assertTrue(arr.containsElem(JsStr.of("a"))); //true
        Assertions.assertTrue(arr.containsElem(NULL));
        Assertions.assertFalse(arr.containsElem(JsInt.of(10)));


        JsArray _arr_ = Jsons.immutable.array.of(JsInt.of(1),
                                                 TRUE,
                                                 JsStr.of("a"),
                                                 NULL
                                                );
        Assertions.assertTrue(_arr_.containsElem(JsInt.of(1)));   //true
        Assertions.assertTrue(_arr_.containsElem(TRUE));   //true
        Assertions.assertTrue(_arr_.containsElem(JsStr.of("a"))); //true
        Assertions.assertTrue(_arr_.containsElem(NULL));
        Assertions.assertFalse(arr.containsElem(JsInt.of(10)));
    }


    @Test
    public void test_empty_immutable_js_array_returns_the_same_instance()
    {

        Assertions.assertSame(Jsons.immutable.array.empty(),
                              Jsons.immutable.array.empty()
                             );
    }


    @Test
    public void test_init_of_json_array_returns_all_the_elements_except_the_last_or_an_exception()
    {

        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );

        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(1),
                                                         TRUE,
                                                         JsStr.of("a")
                                                        ),
                                arr.init()
                               );

        Assertions.assertEquals(Jsons.immutable.array.of(TRUE,
                                                         JsStr.of("a")
                                                        ),
                                arr.tail()
                                   .init()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.empty()
                                                           .init()
                               );


    }


    @Test
    public void test_last_returns_the_last_element_or_throws_exception_if_emtpy()
    {

        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );

        Assertions.assertEquals(NULL,
                                arr.last()
                               );

        Assertions.assertEquals(JsStr.of("a"),
                                arr.init()
                                   .last()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.empty()
                                                           .last()
                               );


    }


    @Test
    public void test_create_immutable_json_array_from_list_of_elements()
    {

        JsArray arr = Jsons.immutable.array.ofIterable(Arrays.asList(JsStr.of("a"),
                                                                     JsInt.of(1)
                                                                    )
                                                      );
        JsArray newArr = arr.remove(JsPath.fromIndex(-1));

        Assertions.assertEquals(2,
                                arr.size()
                               );

        Assertions.assertEquals(1,
                                newArr.size()
                               );

    }

    @Test
    public void test_create_one_element_immutable_json_array()
    {
        JsArray arr = Jsons.immutable.array.of(NULL);
        JsArray arr1 = arr.prepend(JsStr.of("a"));
        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        Assertions.assertEquals(JsStr.of("a"),
                                arr1.head()
                               );

    }

    @Test
    public void test_create_two_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               NULL,
                                               JsInt.of(2),
                                               Jsons.immutable.object.empty()
                                              );
        JsArray newArr = arr.mapElems(p -> p.mapIfInt(i -> i + 10).elem,
                                      p -> p.elem.isInt()
                                     );

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );
        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(11),
                                                         NULL,
                                                         JsInt.of(12),
                                                         Jsons.immutable.object.empty()

                                                        ),
                                newArr
                               );

    }

    @Test
    public void test_create_three_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsStr.of("a"),
                                               JsStr.of("b"),
                                               JsStr.of("c")
                                              );

        final JsArray newArr = arr.mapElems(p -> p.mapIfStr(String::toUpperCase).elem);

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );
        Assertions.assertEquals(Jsons.immutable.array.of("A",
                                                         "B",
                                                         "C"
                                                        ),
                                newArr
                               );


    }

    @Test
    public void test_create_four_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsLong.of(10),
                                               JsStr.of("b"),
                                               JsStr.of("c"),
                                               JsInt.of(10)
                                              );
        JsArray arr1 = arr.filterElems(p -> p.elem.isIntegral());

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        Assertions.assertEquals(Jsons.immutable.array.of(JsLong.of(10),
                                                         JsInt.of(10)
                                                        ),
                                arr1
                               );
    }

    @Test
    public void test_create_five_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsStr.of("A"),
                                               JsStr.of("B"),
                                               JsStr.of("C"),
                                               JsStr.of("D"),
                                               JsStr.of("E")
                                              );
        JsArray arr1 = arr.put(JsPath.fromIndex(-1),
                               "F"
                              );

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );

        Assertions.assertEquals(JsStr.of("F"),
                                arr1.get(JsPath.fromIndex(-1))
                               );

    }

    @Test
    public void test_create_six_elements_imutable_json_array()
    {
        JsArray arr = Jsons.immutable.array.of(JsStr.of("A"),
                                               JsStr.of("B"),
                                               JsStr.of("C"),
                                               JsStr.of("D"),
                                               JsStr.of("E"),
                                               JsStr.of("F"),
                                               JsStr.of("G")
                                              );

        JsArray arr1 = arr.mapElems(pair -> pair.mapIfStr(s ->
                                                          {
                                                              final int index = pair.path.last()
                                                                                         .asIndex().n;
                                                              return s.concat(String.valueOf(index));
                                                          })
                                    .elem
                                   );

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );

        Assertions.assertEquals(Jsons.immutable.array.of(JsStr.of("A0"),
                                                         JsStr.of("B1"),
                                                         JsStr.of("C2"),
                                                         JsStr.of("D3"),
                                                         JsStr.of("E4"),
                                                         JsStr.of("F5"),
                                                         JsStr.of("G6")
                                                        ),
                                arr1
                               );
    }

    @Test
    public void test_parse_string_into_immutable_json_array() throws MalformedJson
    {

        Assertions.assertEquals(Jsons.immutable.array.of(1,
                                                         2
                                                        ),
                                Jsons.immutable.array.parse("[1,2]")
                                                     .orElseThrow()
                               );
        Assertions.assertEquals(Optional.of(Jsons.immutable.array.of(1,
                                                                     2
                                                                    )),
                                Jsons.immutable.array.parse("[1,2]")
                                                     .toOptional()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.array.parse("[1,2")
                                                           .orElseThrow()
                               );


        Assertions.assertEquals(Optional.empty(),
                                Jsons.immutable.array.parse("[1,2")
                                                     .toOptional()
                               ); // Optional.empty


    }

    @Test
    public void test_tail_of_json_array_returns_all_elements_except_first_one()
    {

        JsArray arr = Jsons.immutable.array.of("a",
                                               "b",
                                               "c"
                                              );
        Assertions.assertEquals(Jsons.immutable.array.of("b",
                                                         "c"
                                                        ),
                                arr.tail()
                               ); //  ["b","c"]

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.empty()
                                                           .tail()
                               );


    }

    @Test
    public void test_parse_string_into_immutable_json_array_with_options() throws MalformedJson
    {

        final JsArray arr = Jsons.immutable.array.parse("[1,2,3,true,false,null,[null,true,4]]",
                                                        ParseBuilder.builder()
                                                                    .withElemFilter(p -> p.elem.isInt())
                                                                    .withElemMap(p -> JsElems.mapIfInt(i -> i + 10)
                                                                                             .apply(p.elem))
                                                       )
                                                 .orElseThrow();

        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(11),
                                                         JsInt.of(12),
                                                         JsInt.of(13),
                                                         Jsons.immutable.array.of(14)
                                                        ),
                                arr
                               );

    }


    @Test
    public void test_static_factory_methods_from_primitives()
    {

        JsArray arr = Jsons.immutable.array.of(1,
                                               2,
                                               3
                                              );
        Assertions.assertNotEquals(arr,
                                   arr.append(JsInt.of(4))
                                  );


        JsArray arr1 = Jsons.immutable.array.of("a",
                                                "b",
                                                "c"
                                               );
        Assertions.assertNotEquals(arr1,
                                   arr1.append(JsStr.of("d"))
                                  );




        JsArray arr2 = Jsons.immutable.array.of(true,
                                                false
                                               );
        Assertions.assertNotEquals(arr2,
                                   arr2.append(JsBool.of(false))
                                  );


        JsArray arr3 = Jsons.immutable.array.of(1l,
                                                2l,
                                                3l
                                               );
        Assertions.assertNotEquals(arr3,
                                   arr3.append(JsLong.of(4))
                                  );


        JsArray arr4 = Jsons.immutable.array.of(1.1d,
                                                2.2d,
                                                3.3d
                                               );
        Assertions.assertNotEquals(arr4,
                                   arr4.append(JsDouble.of(4.4d))
                                  );


    }


    @Test
    public void test_create_immutable_json_array_from_one_or_more_pairs() throws MalformedJson
    {

        final JsArray arr = Jsons.immutable.array.of(JsPair.of(JsPath.fromIndex(0),
                                                               JsInt.of(1)
                                                              ),
                                                     JsPair.of(JsPath.fromIndex(2),
                                                               JsInt.of(3)
                                                              )
                                                    );
        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(1),
                                                         NULL,
                                                         JsInt.of(3)
                                                        ),
                                arr
                               );

        final JsArray arr1 = Jsons.immutable.array.of(JsPair.of(path("/0/a"),
                                                                JsInt.of(1)
                                                               ),
                                                      JsPair.of(path("/2/b"),
                                                                JsInt.of(3)
                                                               )
                                                     );

        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"a\": 1},null,{\"b\": 3}]")
                                                     .orElseThrow(),
                                arr1
                               );


    }


    @Test
    public void test_intersection() throws MalformedJson
    {

        final JsArray arr1 = Jsons.immutable.array.parse("[{\"a\": 1, \"b\": [1,2,2]}]")
                                                  .orElseThrow();
        final JsArray arr2 = Jsons.immutable.array.parse("[{\"a\": 1, \"b\": [1,2]}]")
                                                  .orElseThrow();

        Assertions.assertEquals(arr1,
                                arr1.intersection(arr1,
                                                  LIST
                                                 )
                               );
        Assertions.assertEquals(arr2,
                                arr2.intersection(arr2,
                                                  LIST
                                                 )
                               );
        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  LIST
                                                 )
                               );

        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  LIST
                                                 )
                               );

        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  SET
                                                 )
                               );

        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  MULTISET
                                                 )
                               );

        Assertions.assertEquals(arr2,
                                arr1.intersection_(arr2)
                               );


        Assertions.assertEquals(arr2,
                                arr1.intersection_(arr2
                                                  )
                               );


    }

    @Test
    public void test_equals_and_hashcode()
    {
        final JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                                     JsBigInt.of(BigInteger.ONE),
                                                     JsLong.of(1),
                                                     JsBigDec.of(BigDecimal.ONE),
                                                     JsDouble.of(1d)
                                                    );
        final JsArray arr1 = Jsons.immutable.array.of(JsBigDec.of(BigDecimal.ONE),
                                                      JsLong.of(1),
                                                      JsInt.of(1),
                                                      JsBigInt.of(BigInteger.ONE),
                                                      JsDouble.of(1d)
                                                     );

        Assertions.assertEquals(arr,
                                arr1
                               );
        Assertions.assertEquals(arr.hashCode(),
                                arr1.hashCode()
                               );

    }


    @Test
    public void test_reduce_strings()
    {
        final JsArray array = Jsons.immutable.array.of(JsStr.of("a"),
                                                       JsStr.of("b"),
                                                       JsInt.of(1),
                                                       JsInt.of(2),
                                                       Jsons.immutable.object.of("key",
                                                                                 JsStr.of("c"),
                                                                                 "key1",
                                                                                 JsStr.of("d"),
                                                                                 "key3",
                                                                                 Jsons.immutable.array.of("e",
                                                                                                          "f",
                                                                                                          "g"
                                                                                                         )
                                                                                )
                                                      );

        final Optional<String> result = array.reduce_(String::concat,
                                                      p -> p.elem.asJsStr().x,
                                                      p -> p.elem.isStr()
                                                     );

        final char[] chars = result.get()
                                   .toCharArray();

        Arrays.sort(chars);
        Assertions.assertEquals("abcdefg",
                                new String(chars)
                               );

        final Optional<String> result1 = array.reduce(String::concat,
                                                      p -> p.elem.asJsStr().x,
                                                      p -> p.elem.isStr()
                                                     );

        final char[] chars1 = result1.get()
                                     .toCharArray();

        Arrays.sort(chars1);
        Assertions.assertEquals("ab",
                                new String(chars1)
                               );
    }


    @Test
    public void test_filter_immutable_jsons() throws MalformedJson
    {
        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         NULL
                                                                        ),
                                               JsInt.of(1),
                                               Jsons.immutable.object.of("a",
                                                                         NULL
                                                                        ),
                                               Jsons.immutable.object.of("a",
                                                                         JsInt.of(1)
                                                                        ),
                                               Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                                                  NULL
                                                                                                 ),
                                                                        Jsons.immutable.object.of("a",
                                                                                                  JsInt.of(1)
                                                                                                 ),
                                                                        Jsons.immutable.object.of("b",
                                                                                                  NULL
                                                                                                 ),
                                                                        Jsons.immutable.object.of("a",
                                                                                                  NULL
                                                                                                 )
                                                                       )
                                              );


        final JsArray arr1 = arr.filterObjs_((p, o) ->
                                             {
                                                 Assertions.assertEquals(o,
                                                                         arr.get(p)
                                                                        );
                                                 return o.get(JsPath.fromKey("a"))
                                                         .isNotNull();
                                             });
        Assertions.assertEquals(Jsons.immutable.array.parse("[1,{\"a\":1},[{\"a\":1},{\"b\":null}]]\n")
                                                     .orElseThrow(),
                                arr1
                               );


        final JsArray arr2 = arr.filterObjs((p, o) ->
                                            {
                                                Assertions.assertEquals(o,
                                                                        arr.get(p)
                                                                       );
                                                return o.get(JsPath.fromKey("a"))
                                                        .isNotNull();
                                            });

        Assertions.assertEquals(Jsons.immutable.array.parse("[1,{\"a\":1},[{\"a\":null},{\"a\":1},{\"b\":null},{\"a\":null}]]\n")
                                                     .orElseThrow(),
                                arr2
                               );
    }


    @Test
    public void test_parse_with_options_immutable() throws MalformedJson
    {
        final JsArray array = Jsons.immutable.array.of(NULL,
                                                       Jsons.immutable.array.of(1,
                                                                                2
                                                                               ),
                                                       NULL,
                                                       Jsons.immutable.array.of("a",
                                                                                "b"
                                                                               ),
                                                       NULL
                                                      );
        Assertions.assertEquals(array.filterElems_(p ->
                                                   {
                                                       Assertions.assertEquals(p.elem,
                                                                               array.get(p.path)
                                                                              );
                                                       return p.elem.isNotNull();
                                                   }),
                                Jsons.immutable.array.parse(array.toString(),
                                                            ParseBuilder.builder()
                                                                        .withElemFilter(p -> p.elem.isNotNull())
                                                           )
                                                     .orElseThrow()
                               );
    }


    @Test
    public void test_map_json_immutable() throws MalformedJson
    {

        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2)
                                                                        ),
                                               JsStr.of("c"),
                                               TRUE,
                                               FALSE,
                                               Jsons.immutable.object.of("a",
                                                                         Jsons.immutable.object.of("d",
                                                                                                   JsInt.of(1),
                                                                                                   "e",
                                                                                                   JsInt.of(2)
                                                                                                  ),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         JsInt.of(3)
                                                                        )
                                              );

        final JsArray a_ = arr.mapObjs_((path, obj) ->
                                        {
                                            Assertions.assertEquals(obj,
                                                                    arr.get(path)
                                                                   );
                                            return obj.put(JsPath.fromKey("size"),
                                                           obj.size()
                                                          );
                                        });


        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"size\":2,\"d\":1},\"b\":2,\"c\":3}]\n")
                                                     .orElseThrow(),
                                a_
                               );

        final JsArray a = arr.mapObjs((path, obj) ->
                                      {
                                          Assertions.assertEquals(obj,
                                                                  arr.get(path)
                                                                 );
                                          return obj.put(JsPath.fromKey("size"),
                                                         obj.size()
                                                        );
                                      });

        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"d\":1},\"b\":2,\"c\":3}]\n")
                                                     .orElseThrow(),
                                a
                               );
    }

    @Test
    public void test_map_json_immutable_with_predicate() throws MalformedJson
    {

        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         Jsons.immutable.object.empty()
                                                                        ),
                                               Jsons.immutable.object.empty(),
                                               JsStr.of("c"),
                                               TRUE,
                                               FALSE,
                                               Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         JsInt.of(3)
                                                                        )
                                              );

        final JsArray a = arr.mapObjs_((path, obj) ->
                                       {
                                           Assertions.assertEquals(obj,
                                                                   arr.get(path)
                                                                  );
                                           return obj.put(JsPath.fromKey("size"),
                                                          obj.size()
                                                         );
                                       },
                                       (p, obj) -> obj.isNotEmpty()
                                      );


        Assertions.assertEquals(Jsons.immutable.array.parse("[\n"
                                                            + "  {\n"
                                                            + "    \"size\": 3,\n"
                                                            + "    \"a\": 1,\n"
                                                            + "    \"b\": 2,\n"
                                                            + "    \"c\": {}\n"
                                                            + "  },\n"
                                                            + "  {},\n"
                                                            + "  \"c\",\n"
                                                            + "  true,\n"
                                                            + "  false,\n"
                                                            + "  {\n"
                                                            + "    \"size\": 3,\n"
                                                            + "    \"a\": 1,\n"
                                                            + "    \"b\": 2,\n"
                                                            + "    \"c\": 3\n"
                                                            + "  }\n"
                                                            + "]\n")
                                                     .orElseThrow(),
                                a
                               );
    }


    @Test
    public void test_map_json_immutable_array() throws MalformedJson
    {
        final JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                               Jsons.immutable.object.empty(),
                                                                               "b",
                                                                               JsStr.of("B")
                                                                              ),
                                                     NULL,
                                                     Jsons.immutable.object.empty(),
                                                     Jsons.immutable.array.empty(),
                                                     Jsons.immutable.object.of("c",
                                                                               JsStr.of("C"),
                                                                               "d",
                                                                               JsStr.of("D"),
                                                                               "e",
                                                                               Jsons.immutable.object.of("f",
                                                                                                         JsStr.of("F")
                                                                                                        )
                                                                              )
                                                    );

        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put(JsPath.fromKey("size"),
                                                                                    json.size()
                                                                                   );

        final JsArray newArr = arr.mapObjs((p, o) ->
                                           {
                                               Assertions.assertEquals(o,
                                                                       arr.get(p)
                                                                      );
                                               return addSizeFn.apply(p,
                                                                      o
                                                                     );
                                           },
                                           (p, o) -> o.isNotEmpty()
                                          );

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );

        Assertions.assertEquals(Jsons.immutable.parse("[{\"size\":2,\"a\":{},\"b\":\"B\"},null,{},[],{\"e\":{\"f\":\"F\"},\"size\":3,\"c\":\"C\",\"d\":\"D\"}]\n")
                                               .arrOrElseThrow(),
                                newArr
                               );

        final JsArray arr1 = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                                Jsons.immutable.object.empty(),
                                                                                "b",
                                                                                JsStr.of("B")
                                                                               ),
                                                      NULL,
                                                      Jsons.immutable.object.empty(),
                                                      Jsons.immutable.array.empty(),
                                                      Jsons.immutable.object.of("c",
                                                                                Jsons.immutable.array.empty(),
                                                                                "d",
                                                                                JsStr.of("D"),
                                                                                "e",
                                                                                Jsons.immutable.object.of("f",
                                                                                                          JsStr.of("F"),
                                                                                                          "g",
                                                                                                          Jsons.immutable.object.of("h",
                                                                                                                                    JsStr.of("H")
                                                                                                                                   )
                                                                                                         )
                                                                               )
                                                     );

        final JsArray newArr1 = arr1.mapObjs_((p, o) ->
                                              {
                                                  Assertions.assertEquals(o,
                                                                          arr1.get(p)
                                                                         );
                                                  return addSizeFn.apply(p,
                                                                         o
                                                                        );
                                              },
                                              (p, o) -> o.isNotEmpty()
                                             );


        Assertions.assertNotEquals(arr1,
                                   newArr1
                                  );

        Assertions.assertEquals(Jsons.immutable.parse("[{\"size\":2,\"a\":{},\"b\":\"B\"},null,{},[],{\"e\":{\"size\":2,\"f\":\"F\",\"g\":{\"size\":1,\"h\":\"H\"}},\"size\":3,\"c\":[],\"d\":\"D\"}]\n")
                                               .arrOrElseThrow(),
                                newArr1
                               );
    }


    @Test
    public void test_operations_immutable()
    {

        JsArray array = Jsons.immutable.array.of(JsPair.of(path("/0/b/0"),
                                                           JsInt.of(1)
                                                          )
                                                );

        Assertions.assertEquals(array,
                                array.remove(path("/0/b/c"))
                               );

        Assertions.assertEquals(array,
                                array.remove(path("/0/0/c"))
                               );

        Assertions.assertEquals(array,
                                array.remove(path("/0/b/0/a"))
                               );
    }


}
