package jsonvalues;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsPath.path;

public class TestJsArray {


    @Test
    public void test_set_and_get_with_padding() {

        JsArray array = JsArray.empty()
                               .set(2,
                                    JsInt.of(1),
                                    JsInt.of(0)
                                   );
        Assertions.assertEquals(0,
                                array.getInt(0)
                               );
        Assertions.assertEquals(0,
                                array.getInt(1)
                               );
        Assertions.assertEquals(1,
                                array.getInt(2)
                               );


    }

    @Test
    public void test_contains_element_in_js_array() {
        JsArray arr = JsArray.of(JsInt.of(1),
                                 TRUE,
                                 JsStr.of("a"),
                                 NULL
                                );
        Assertions.assertTrue(arr.containsValue(JsInt.of(1)));   //true
        Assertions.assertTrue(arr.containsValue(TRUE));   //true
        Assertions.assertTrue(arr.containsValue(JsStr.of("a"))); //true
        Assertions.assertTrue(arr.containsValue(NULL));
        Assertions.assertFalse(arr.containsValue(JsInt.of(10)));


        JsArray _arr_ = JsArray.of(JsInt.of(1),
                                   TRUE,
                                   JsStr.of("a"),
                                   NULL
                                  );
        Assertions.assertTrue(_arr_.containsValue(JsInt.of(1)));   //true
        Assertions.assertTrue(_arr_.containsValue(TRUE));   //true
        Assertions.assertTrue(_arr_.containsValue(JsStr.of("a"))); //true
        Assertions.assertTrue(_arr_.containsValue(NULL));
        Assertions.assertFalse(arr.containsValue(JsInt.of(10)));
    }

    @Test
    public void test_create_four_elements_json_array() {

        JsArray arr = JsArray.of(JsLong.of(10),
                                 JsStr.of("b"),
                                 JsStr.of("c"),
                                 JsInt.of(10)
                                );
        JsArray arr1 = arr.filterValues(JsValue::isIntegral);
        JsArray arr2 = arr.filterValues((p, v) -> {
            Assertions.assertEquals(v,
                                    arr.get(p)
                                   );
            return v.isIntegral();
        });

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        JsArray expected = JsArray.of(JsLong.of(10),
                                      JsInt.of(10)
                                     );
        Assertions.assertEquals(expected,
                                arr1
                               );
        Assertions.assertEquals(expected,
                                arr2
                               );
    }


    @Test
    public void test_create_json_array_from_list_of_elements() {

        JsArray arr = JsArray.ofIterable(Arrays.asList(JsStr.of("a"),
                                                       JsInt.of(1)
                                                      )
                                        );
        JsArray newArr = arr.delete(arr.size() - 1);

        Assertions.assertEquals(2,
                                arr.size()
                               );

        Assertions.assertEquals(1,
                                newArr.size()
                               );

    }


    @Test
    public void test_create_one_element_json_array() {
        JsArray arr = JsArray.of(NULL);
        JsArray arr1 = arr.prepend(JsStr.of("a"),
                                   JsInt.of(10)
                                  );
        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        Assertions.assertEquals(JsStr.of("a"),
                                arr1.head()
                               );

    }

    @Test
    public void testPrepend() {
        JsArray empty = JsArray.empty();
        JsArray a = empty.prepend(JsStr.of("a"),
                                  JsInt.of(1)
                                 );
        Assertions.assertEquals(JsInt.of(1),
                                a.last()
                               );
        Assertions.assertEquals(JsStr.of("a"),
                                a.head()
                               );
        JsArray b = a.prependAll(JsArray.of(-1,
                                            -2
                                           ));
        Assertions.assertEquals(JsArray.of(JsInt.of(-1),
                                           JsInt.of(-2),
                                           JsStr.of("a"),
                                           JsInt.of(1)
                                          ),
                                b
                               );

    }

    @Test
    public void test_create_six_elements_json_array() {
        JsArray arr = JsArray.of(JsStr.of("A"),
                                 JsStr.of("B"),
                                 JsStr.of("C"),
                                 JsStr.of("D"),
                                 JsStr.of("E"),
                                 JsStr.of("F"),
                                 JsStr.of("G")
                                );

        JsArray arr1 = arr.mapValues((i, val) -> {
            Assertions.assertEquals(val,
                                    arr.get(i)
                                   );
            return JsStr.prism.modify.apply(s -> s.concat(String.valueOf(i.head())))
                                     .apply(val);
        });

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );

        Assertions.assertEquals(JsArray.of(JsStr.of("A0"),
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
    public void test_create_three_elements_json_array() {

        JsArray arr = JsArray.of(JsStr.of("a"),
                                 JsStr.of("b"),
                                 JsStr.of("c")
                                );

        JsArray newArr = arr.mapValues((p, val) -> {
            Assertions.assertEquals(val,
                                    arr.get(p)
                                   );

            return JsStr.prism.modify.apply(String::toUpperCase)
                                     .apply(val);
        });

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );
        Assertions.assertEquals(JsArray.of("A",
                                           "B",
                                           "C"
                                          ),
                                newArr
                               );


    }

    @Test
    public void test_create_two_elements_json_array() {

        JsArray arr = JsArray.of(JsInt.of(1),
                                 NULL,
                                 JsInt.of(2),
                                 JsObj.empty()
                                );
        JsArray newArr = arr.mapValues((p, val) -> {
            Assertions.assertEquals(val,
                                    arr.get(p)
                                   );

            return JsInt.prism.modify.apply(i -> i + 10)
                                     .apply(val);
        });

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );
        Assertions.assertEquals(JsArray.of(JsInt.of(11),
                                           NULL,
                                           JsInt.of(12),
                                           JsObj.empty()

                                          ),
                                newArr
                               );

    }

    @Test
    public void test_creation_two_element_json_array() {
        JsArray arr = JsArray.of(JsInt.of(1),
                                 TRUE,
                                 JsInt.of(2),
                                 JsStr.of("a"),
                                 NULL,
                                 JsStr.of("h"),
                                 JsObj.of("a",
                                          JsInt.of(1),
                                          "b",
                                          JsInt.of(2),
                                          "c",
                                          NULL
                                         )
                                );


        int result2 = arr.mapValues((p, val) -> {
                             Assertions.assertEquals(val,
                                                     arr.get(p)
                                                    );

                             return JsInt.prism.modify.apply(i -> i + 100)
                                                      .apply(val);
                         })
                         .reduce(Integer::sum,
                                 (p, v) -> v.toJsInt().value,
                                 (p, v) -> v.isInt()
                                )
                         .orElse(-1);

        int result3 = arr.mapValues(val -> JsInt.prism.modify.apply(i -> i + 100)
                                                             .apply(val)
                                   )
                         .reduce(Integer::sum,
                                 v -> v.toJsInt().value,
                                 JsValue::isInt
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
    public void test_empty_js_array_returns_the_same_instance() {

        Assertions.assertSame(JsArray.empty(),
                              JsArray.empty()
                             );
    }

    @Test
    public void test_equals_and_hashcode() {
        JsArray arr = JsArray.of(JsInt.of(1),
                                 JsBigInt.of(BigInteger.ONE),
                                 JsLong.of(1),
                                 JsBigDec.of(BigDecimal.ONE),
                                 JsDouble.of(1d)
                                );
        JsArray arr1 = JsArray.of(JsBigDec.of(BigDecimal.ONE),
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
    public void test_filter_jsons() {
        JsArray arr = JsArray.of(JsObj.of("a",
                                          NULL
                                         ),
                                 JsInt.of(1),
                                 JsObj.of("a",
                                          NULL
                                         ),
                                 JsObj.of("a",
                                          JsInt.of(1)
                                         ),
                                 JsArray.of(JsObj.of("a",
                                                     NULL
                                                    ),
                                            JsObj.of("a",
                                                     JsInt.of(1)
                                                    ),
                                            JsObj.of("b",
                                                     NULL
                                                    ),
                                            JsObj.of("a",
                                                     NULL
                                                    )
                                           )
                                );


        JsArray arr1 = arr.filterObjs((p, o) ->
                                      {
                                          Assertions.assertEquals(o,
                                                                  arr.get(p)
                                                                 );
                                          return o.get("a")
                                                  .isNotNull();
                                      });
        Assertions.assertEquals(JsArray.parse("[1,{\"a\":1},[{\"a\":1},{\"b\":null}]]\n"),
                                arr1
                               );

        JsArray arr2 = arr.filterObjs(o -> o.get("a")
                                            .isNotNull());
        Assertions.assertEquals(JsArray.parse("[1,{\"a\":1},[{\"a\":1},{\"b\":null}]]\n"),
                                arr2
                               );


    }

    @Test
    public void test_init_of_json_array_returns_all_the_elements_except_the_last_or_an_exception() {

        JsArray arr = JsArray.of(JsInt.of(1),
                                 TRUE,
                                 JsStr.of("a"),
                                 NULL
                                );

        Assertions.assertEquals(JsArray.of(JsInt.of(1),
                                           TRUE,
                                           JsStr.of("a")
                                          ),
                                arr.init()
                               );

        Assertions.assertEquals(JsArray.of(TRUE,
                                           JsStr.of("a")
                                          ),
                                arr.tail()
                                   .init()
                               );


    }

    @Test
    public void test_intersection() {

        JsArray arr1 = JsArray.parse("[{\"a\": 1, \"b\": [1,2,2]}]");
        JsArray arr2 = JsArray.parse("[{\"a\": 1, \"b\": [1,2]}]");

        Assertions.assertEquals(arr2,
                                arr1.intersection(arr2,
                                                  JsArray.TYPE.SET
                                                 )
                               );


        Assertions.assertEquals(arr2,
                                arr1.intersection(arr2,
                                                  JsArray.TYPE.LIST
                                                 )
                               );


    }

    @Test
    public void test_last_returns_the_last_element_or_throws_exception_if_emtpy() {

        JsArray arr = JsArray.of(JsInt.of(1),
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


    }

    @Test
    public void test_map_json_with_path() {

        JsArray arr = JsArray.of(JsObj.of("a",
                                          JsInt.of(1),
                                          "b",
                                          JsInt.of(2)
                                         ),
                                 JsStr.of("c"),
                                 TRUE,
                                 FALSE,
                                 JsObj.of("a",
                                          JsObj.of("d",
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

        JsArray a = arr.mapObjs((path, obj) ->
                                {
                                    Assertions.assertEquals(obj,
                                                            arr.get(path)
                                                           );
                                    return obj.set("size",
                                                   JsInt.of(obj.size())
                                                  );
                                });


        Assertions.assertEquals(JsArray.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"size\":2,\"d\":1},\"b\":2,\"c\":3}]\n"),
                                a
                               );


    }

    @Test
    public void test_map_all_json_with_path() {

        JsArray arr = JsArray.of(JsObj.of("a",
                                          JsInt.of(1),
                                          "b",
                                          JsInt.of(2)
                                         ),
                                 JsStr.of("c"),
                                 TRUE,
                                 FALSE,
                                 JsObj.of("a",
                                          JsObj.of("d",
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

        JsArray a = arr.mapObjs((p, obj) ->
                                {
                                    Assertions.assertEquals(obj,
                                                            arr.get(p)
                                                           );
                                    return obj.set("size",
                                                   JsInt.of(obj.size())
                                                  );
                                });


        Assertions.assertEquals(JsArray.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"size\":2,\"d\":1},\"b\":2,\"c\":3}]\n")
                ,
                                a
                               );


    }

    @Test
    public void test_map_all_json() {

        JsArray arr = JsArray.of(JsObj.of("a",
                                          JsInt.of(1),
                                          "b",
                                          JsInt.of(2)
                                         ),
                                 JsStr.of("c"),
                                 TRUE,
                                 FALSE,
                                 JsObj.of("a",
                                          JsObj.of("d",
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

        JsArray a = arr.mapObjs(obj ->
                                        obj.set("size",
                                                JsInt.of(obj.size())
                                               ));


        Assertions.assertEquals(
                JsArray.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"size\":2,\"d\":1},\"b\":2,\"c\":3}]\n"),
                a
                               );


    }

    @Test
    public void test_map_json_with_predicate() {

        JsArray arr = JsArray.of(JsObj.of("a",
                                          JsInt.of(1),
                                          "b",
                                          JsInt.of(2),
                                          "c",
                                          JsObj.empty()
                                         ),
                                 JsObj.empty(),
                                 JsStr.of("c"),
                                 TRUE,
                                 FALSE,
                                 JsObj.of("a",
                                          JsInt.of(1),
                                          "b",
                                          JsInt.of(2),
                                          "c",
                                          JsInt.of(3)
                                         )
                                );

        JsArray a = arr.mapObjs((path, obj) ->
                                {

                                    Assertions.assertEquals(obj,
                                                            arr.get(path)
                                                           );
                                    if (obj.isEmpty()) return obj;
                                    return obj.set("size",
                                                   JsInt.of(obj.size())
                                                  );
                                }
                               );


        Assertions.assertEquals(JsArray.parse("[\n"
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
                ,
                                a
                               );
    }

    @Test
    public void test_operations() {

        JsArray array = JsArray.empty().set(path("/0/b/0"),
                                            JsInt.of(1)

                                           );

        Assertions.assertEquals(array,
                                array.delete(path("/0/b/c"))
                               );

        Assertions.assertEquals(array,
                                array.delete(path("/0/0/c"))
                               );

        Assertions.assertEquals(array,
                                array.delete(path("/0/b/0/a"))
                               );
    }

    @Test
    public void test_parse_string_into_json_array() {

        Assertions.assertEquals(JsArray.of(1,
                                           2
                                          ),
                                JsArray.parse("[1,2]")
                               );
        Assertions.assertEquals(JsArray.of(1,
                                           2
                                          ),
                                JsArray.parse("[1,2]")

                               );
        Assertions.assertThrows(JsParserException.class,
                                () -> JsArray.parse("[1,2")
                               );

        Assertions.assertThrows(JsParserException.class,
                                () -> JsArray.parse("[1,2")
                               );


    }

    @Test
    public void test_reduce_strings() {
        JsObj of = JsObj.of("key",
                            JsStr.of("c"),
                            "key1",
                            JsStr.of("d"),
                            "key3",
                            JsArray.of("e",
                                       "f",
                                       "g"
                                      )
                           );
        JsArray array = JsArray.of(JsStr.of("a"),
                                   JsStr.of("b"),
                                   JsInt.of(1),
                                   JsInt.of(2),
                                   of
                                  );

        Optional<String> result = array.reduce(String::concat,
                                               (p, v) -> v.toJsStr().value,
                                               (p, v) -> v.isStr()
                                              );

        char[] chars = result.get()
                             .toCharArray();

        Arrays.sort(chars);
        Assertions.assertEquals("abcdefg",
                                new String(chars)
                               );


    }

    @Test
    public void test_static_factory_methods_from_primitives() {

        JsArray arr = JsArray.of(1,
                                 2,
                                 3
                                );
        Assertions.assertNotEquals(arr,
                                   arr.append(JsInt.of(4))
                                  );


        JsArray arr1 = JsArray.of("a",
                                  "b",
                                  "c"
                                 );
        Assertions.assertNotEquals(arr1,
                                   arr1.append(JsStr.of("d"))
                                  );


        JsArray arr2 = JsArray.of(true,
                                  false
                                 );
        Assertions.assertNotEquals(arr2,
                                   arr2.append(JsBool.of(false))
                                  );


        JsArray arr3 = JsArray.of(1L,
                                  2L,
                                  3L
                                 );
        Assertions.assertNotEquals(arr3,
                                   arr3.append(JsLong.of(4))
                                  );


        JsArray arr4 = JsArray.of(1.1d,
                                  2.2d,
                                  3.3d
                                 );
        Assertions.assertNotEquals(arr4,
                                   arr4.append(JsDouble.of(4.4d))
                                  );


    }

    @Test
    public void test_tail_of_json_array_returns_all_elements_except_first_one() {

        JsArray arr = JsArray.of("a",
                                 "b",
                                 "c"
                                );
        Assertions.assertEquals(JsArray.of("b",
                                           "c"
                                          ),
                                arr.tail()
                               ); //  ["b","c"]


    }

    @Test
    public void test_parse_array_of_bigints() {
        JsArray arr = JsArray.parse("[-8354817123538400257,9223372036854775807,-1,0,-8871622059039849388]");

        Assertions.assertEquals(JsArray.parse(arr.toString()),
                                arr
                               );

    }


    @Test
    public void test_map_all_keys() {

        JsArray a = JsArray.of(1,
                               2,
                               3,
                               4
                              );

        Function<String, String> toUpperCase = String::toUpperCase;
        Assertions.assertEquals(a,
                                a.mapKeys(toUpperCase)
                               );

        JsArray b = JsArray.of(JsObj.of("a",
                                        JsInt.of(1),
                                        "b",
                                        JsInt.of(2)
                                       ),
                               JsArray.of(JsStr.of("a"),
                                          JsObj.of("c",
                                                   TRUE,
                                                   "d",
                                                   FALSE
                                                  )
                                         )
                              );


        JsArray c = b.mapKeys(toUpperCase);

        Assertions.assertTrue(c.stream()
                               .filter(p -> p.path().last()
                                             .isKey())
                               .map(it -> it.path().last()
                                            .asKey().name)
                               .allMatch(key -> key.toUpperCase()
                                                   .equals(key)));

    }

    @Test
    public void test_map_all_keys_with_path() {

        JsArray a = JsArray.of(1,
                               2,
                               3,
                               4
                              );

        Assertions.assertEquals(a,
                                a.mapKeys(key -> key.toUpperCase())
                               );

        JsArray b = JsArray.of(JsObj.of("a",
                                        JsInt.of(1),
                                        "b",
                                        JsInt.of(2)
                                       ),
                               JsArray.of(JsStr.of("a"),
                                          JsObj.of("c",
                                                   TRUE,
                                                   "d",
                                                   FALSE
                                                  )
                                         )
                              );


        JsArray c = b.mapKeys((p, val) -> {
            Assertions.assertEquals(val,
                                    b.get(p)
                                   );

            Assertions.assertEquals(val,
                                    b.get(p)
                                   );
            return p.last()
                    .asKey().name.toUpperCase();

        });

        Assertions.assertTrue(c.stream()
                               .filter(p -> {
                                   Assertions.assertEquals(p.value(),
                                                           c.get(p.path())
                                                          );
                                   return p.path().last()
                                           .isKey();
                               })
                               .map(it -> it.path().last()
                                            .asKey().name)
                               .allMatch(key -> key.toUpperCase()
                                                   .equals(key)));

    }

    @Test
    public void test_get_by_index() {
        JsArray a = JsArray.of(JsInt.of(1),
                               JsStr.of("a"),
                               JsLong.of(Long.MAX_VALUE),
                               TRUE,
                               JsObj.empty(),
                               JsBigDec.of(new BigDecimal("3.5")),
                               JsBigInt.of(new BigInteger("100000000000000000000000000")),
                               JsArray.empty()
                              );
        Assertions.assertNull(a.getBigDec(1));
        Assertions.assertEquals(new BigDecimal("3.5"),
                                a.getBigDec(5)
                               );
        Assertions.assertNull(a.getBigDec(-1));

        Assertions.assertNull(a.getDouble(1));
        Assertions.assertTrue(a.getDouble(5) == 3.5);
        Assertions.assertNull(a.getDouble(-1));

        Assertions.assertNull(a.getBigInt(1));
        Assertions.assertEquals(new BigInteger("100000000000000000000000000"),
                                a.getBigInt(6)
                               );

        Assertions.assertNull(a.getInt(1));
        Assertions.assertEquals(Integer.valueOf(1),
                                a.getInt(0)
                               );

        Assertions.assertNull(a.getLong(1));
        Assertions.assertEquals(Long.valueOf(Long.MAX_VALUE),
                                a.getLong(2)
                               );

        Assertions.assertNull(a.getBool(1));
        Assertions.assertEquals(Boolean.TRUE,
                                a.getBool(3)
                               );

        Assertions.assertNull(a.getStr(0));
        Assertions.assertEquals("a",
                                a.getStr(1)
                               );
        Assertions.assertNull(a.getObj(3));
        Assertions.assertEquals(JsObj.empty(),
                                a.getObj(4)
                               );

        Assertions.assertNull(a.getArray(0));
        Assertions.assertEquals(JsArray.empty(),
                                a.getArray(7)
                               );
    }

    @Test
    public void test_get_instant_by_index() {

        Instant now = Instant.now();
        JsArray array = JsArray.of(JsStr.of("a"),
                                   JsStr.of(now.toString()),
                                   JsInstant.of(now)
                                  );

        Assertions.assertEquals(now,
                                array.getInstant(1)
                               );

        Assertions.assertNull(array.getInstant(0));

    }

    @Test
    public void test_get_binary_by_index() {
        byte[] bytes = "hola".getBytes(StandardCharsets.UTF_8);

        JsArray array = JsArray.of(JsStr.of("a"),
                                   JsStr.of(Base64.getEncoder()
                                                  .encodeToString(bytes)),
                                   JsBinary.of(bytes)
                                  );

        Assertions.assertArrayEquals(bytes,
                                     array.getBinary(1)
                                    );

        Assertions.assertNull(array.getBinary(0));
    }

    @Test
    public void test_filter_all_values() {

        JsArray array = JsArray.of(JsStr.of("a"),
                                   JsStr.of("b"),
                                   JsObj.of("a",
                                            JsStr.of("c"),
                                            "b",
                                            TRUE,
                                            "c",
                                            JsInt.of(1),
                                            "d",
                                            JsArray.of("a"),
                                            "1",
                                            JsArray.of(JsObj.of("1",
                                                                JsStr.of("a")
                                                               ))
                                           ),
                                   FALSE,
                                   JsLong.of(1)
                                  );

        JsArray result = array.filterValues(v -> v.isStr());

        JsArray expected = JsArray.of(JsStr.of("a"),
                                      JsStr.of("b"),
                                      JsObj.of("a",
                                               JsStr.of("c"),
                                               "d",
                                               JsArray.of("a"),
                                               "1",
                                               JsArray.of(JsObj.of("1",
                                                                   JsStr.of("a")
                                                                  ))
                                              )
                                     );
        Assertions.assertEquals(expected,
                                result
                               );

        JsArray result1 = array.filterValues((p, v) -> {
            Assertions.assertEquals(v,
                                    array.get(p)
                                   );
            return v.isStr();
        });


        Assertions.assertEquals(expected,
                                result1
                               );

    }

    @Test
    public void test_filter_all_keys() {

        JsArray array = JsArray.of(JsStr.of("a"),
                                   JsStr.of("b"),
                                   JsObj.of("a",
                                            JsStr.of("c"),
                                            "b1",
                                            TRUE,
                                            "c1",
                                            JsInt.of(1),
                                            "d1",
                                            JsArray.of("a"),
                                            "12",
                                            JsArray.of(JsObj.of("1",
                                                                JsStr.of("a"),
                                                                "22",
                                                                JsInt.of(10)
                                                               ))
                                           ),
                                   FALSE,
                                   JsLong.of(1)
                                  );

        JsArray result = array.filterKeys(k -> k.length() > 1);
        JsArray result1 = array.filterKeys((p, v) -> {
            Assertions.assertEquals(v,
                                    array.get(p)
                                   );
            return p.last()
                    .isKey(k -> k.length() > 1);
        });

        JsArray expected = JsArray.of(JsStr.of("a"),
                                      JsStr.of("b"),
                                      JsObj.of("b1",
                                               TRUE,
                                               "c1",
                                               JsInt.of(1),
                                               "d1",
                                               JsArray.of("a"),
                                               "12",
                                               JsArray.of(JsObj.of("22",
                                                                   JsInt.of(10)
                                                                  )
                                                         )
                                              ),
                                      FALSE,
                                      JsLong.of(1)
                                     );
        Assertions.assertEquals(expected,
                                result
                               );
        Assertions.assertEquals(expected,
                                result1
                               );

        Assertions.assertEquals(array,
                                array.filterKeys(k -> true)
                               );

    }

    @Test
    public void getPrimitivesWithDefaultValue() {

        Instant now = Instant.now();

        byte[] bytes = "hi".getBytes(StandardCharsets.UTF_8);

        JsArray array = JsArray.of(JsObj.of("a",
                                            JsStr.of("hi")
                                           ));
        JsObj obj = JsObj.of("a",
                             NULL
                            );
        JsArray arr = JsArray.of(JsInt.of(1),
                                 array,
                                 JsLong.of(Long.MAX_VALUE),
                                 TRUE,
                                 JsStr.of("bye!"),
                                 JsBigDec.of(BigDecimal.valueOf(1.5d)),
                                 JsDouble.of(0.5),
                                 JsInstant.of(now),
                                 JsBinary.of(bytes),
                                 JsBigInt.of(BigInteger.valueOf(Integer.MAX_VALUE)),
                                 obj
                                );

        Assertions.assertEquals(1,
                                arr.getInt(0,
                                           () -> 0
                                          )
                               );

        Assertions.assertEquals(array,
                                arr.getArray(1,
                                             JsArray::empty
                                            )
                               );

        Assertions.assertEquals(Long.MAX_VALUE,
                                arr.getLong(2,
                                            () -> 0L
                                           )
                               );

        Assertions.assertEquals(true,
                                arr.getBool(3,
                                            () -> false
                                           )
                               );

        Assertions.assertEquals("bye!",
                                arr.getStr(4,
                                           () -> "hi!"
                                          )
                               );

        Assertions.assertEquals(BigDecimal.valueOf(1.5d),
                                arr.getBigDec(5,
                                              () -> BigDecimal.ZERO
                                             )
                               );

        Assertions.assertEquals(0.5d,
                                arr.getDouble(6,
                                              () -> 0.5d
                                             )
                               );

        Assertions.assertEquals(now,
                                arr.getInstant(7,
                                               Instant::now
                                              )
                               );

        Assertions.assertEquals(bytes,
                                arr.getBinary(8,
                                              () -> bytes
                                             )
                               );

        Assertions.assertEquals(BigInteger.valueOf(Integer.MAX_VALUE),
                                arr.getBigInt(9,
                                              () -> BigInteger.ONE
                                             )
                               );

        Assertions.assertEquals(obj,
                                arr.getObj(10,
                                           () -> obj
                                          )
                               );

    }

    @Test
    public void testFilterValues() {

        JsArray a = JsArray.of(JsInt.of(0),
                               JsInt.of(10),
                               JsArray.of(1,
                                          2,
                                          3,
                                          4,
                                          5,
                                          6,
                                          7,
                                          8
                                       ,
                                          9,
                                          10
                                         ),
                               JsInt.of(1),
                               JsArray.of(JsObj.of("a",
                                                   JsInt.of(10),
                                                   "b",
                                                   JsInt.of(1)
                                                  ))
                              );

        JsArray b = a.filterValues(JsInt.prism.exists.apply(n -> n > 5));

        System.out.println(b);

        Assertions.assertEquals(JsArray.of(JsInt.of(10),
                                           JsArray.of(6,
                                                      7,
                                                      8,
                                                      9,
                                                      10
                                                     ),
                                           JsArray.of(JsObj.of("a",
                                                               JsInt.of(10)
                                                              ))
                                          ),
                                b
                               );


    }

    @Test
    public void testMapValues() {

        JsArray a = JsArray.of(JsInt.of(0),
                               JsInt.of(10),
                               JsArray.of(1,
                                          2,
                                          3,
                                          4,
                                          5,
                                          6,
                                          7,
                                          8
                                       ,
                                          9,
                                          10
                                         ),
                               JsInt.of(1),
                               JsArray.of(JsObj.of("a",
                                                   JsInt.of(10),
                                                   "b",
                                                   JsInt.of(1)
                                                  ))
                              );


        JsArray result = JsArray.of(JsInt.of(1),
                                    JsInt.of(11),
                                    JsArray.of(2,
                                               3,
                                               4,
                                               5,
                                               6,
                                               7,
                                               8,
                                               9,
                                               10,
                                               11
                                              ),
                                    JsInt.of(2),
                                    JsArray.of(JsObj.of("a",
                                                        JsInt.of(11),
                                                        "b",
                                                        JsInt.of(2)
                                                       ))
                                   );
        Assertions.assertEquals(result,
                                a.mapValues(JsInt.prism.modify.apply(n -> n + 1))
                               );


        Assertions.assertEquals(result,
                                a.mapValues((path, value) -> {
                                    Assertions.assertEquals(value,
                                                            a.get(path)
                                                           );
                                    return JsInt.prism.modify.apply(n -> n + 1).apply(value);
                                })
                               );
    }


    @Test
    public void testMapObjs() {
        JsArray a = JsArray.of(JsArray.of(JsObj.of("a",
                                                   TRUE,
                                                   "b",
                                                   FALSE
                                                  ),
                                          JsObj.of("c",
                                                   FALSE
                                                  )
                                         ),
                               JsObj.of("a",
                                        NULL
                                       ),
                               FALSE
                              );


        JsArray result = JsArray.of(JsArray.of(JsObj.of("a",
                                                        TRUE,
                                                        "b",
                                                        FALSE,
                                                        "size",
                                                        JsInt.of(2)
                                                       ),
                                               JsObj.of("c",
                                                        FALSE,
                                                        "size",
                                                        JsInt.of(1)
                                                       )
                                              ),
                                    JsObj.of("a",
                                             NULL,
                                             "size",
                                             JsInt.of(1)
                                            ),
                                    FALSE
                                   );
        Assertions.assertEquals(result,
                                a.mapObjs(it -> it.set("size",
                                                       JsInt.of(it.size())
                                                      ))
                               );

        Assertions.assertEquals(result,
                                a.mapObjs((path, value) -> {
                                    Assertions.assertEquals(value, a.get(path));
                                    return value.set("size",
                                                     JsInt.of(value.size())
                                                    );
                                })
                               );


    }

    @Test
    public void testStreamOfValues() {

        Assertions.assertTrue(JsArray.of(1, 2, 3, 4, 5).streamOfValues().allMatch(it -> it.isInt()));
        Assertions.assertEquals(15, JsArray.of(1, 2, 3, 4, 5).streamOfValues().map(it -> it.toJsInt().value)
                                           .reduce(0, (a, b) -> a + b));

    }


}
