package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static jsonvalues.JsArray.TYPE.SET;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsPath.fromKey;
import static jsonvalues.JsPath.path;

public class TestJsObj {


    @Test
    public void test_creates_empty_object() {
        JsObj obj = JsObj.empty();
        Assertions.assertTrue(obj.isEmpty());

        final JsObj obj1 = obj.set("a",
                                   JsInt.of(1)
        );// obj is mutable
        Assertions.assertTrue(obj.isEmpty());
        Assertions.assertTrue(obj1.getInt("a")
                                  .equals(1)
        );
    }

    @Test
    public void test_creates_five_elements_object_and_map_keys_with_path() {

        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsStr.of("B"),
                             "c",
                             JsStr.of("C"),
                             "d",
                             JsStr.of("D"),
                             "e",
                             JsStr.of("E")
        );

        Assertions.assertEquals(5,
                                obj.size()
        );
        final JsObj obj1 = obj.mapKeys((key, val) -> key.toUpperCase());

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("A",
                                 "B",
                                 "C",
                                 "D",
                                 "E"
        ));

        Assertions.assertEquals(set,
                                obj1.keySet()
        );
        Set<String> set1 = new HashSet<>();
        set1.addAll(Arrays.asList("a",
                                  "b",
                                  "c",
                                  "d",
                                  "e"
        ));
        Assertions.assertEquals(set1,
                                obj.keySet()
        );
    }

    @Test
    public void test_creates_five_elements_object_and_map_keys() {

        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsStr.of("B"),
                             "c",
                             JsStr.of("C"),
                             "d",
                             JsStr.of("D"),
                             "e",
                             JsStr.of("E")
        );

        Assertions.assertEquals(5,
                                obj.size()
        );
        final JsObj obj1 = obj.mapKeys((Function<String, String>) String::toUpperCase);

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("A",
                                 "B",
                                 "C",
                                 "D",
                                 "E"
        ));

        Assertions.assertEquals(set,
                                obj1.keySet()
        );
        Set<String> set1 = new HashSet<>();
        set1.addAll(Arrays.asList("a",
                                  "b",
                                  "c",
                                  "d",
                                  "e"
        ));
        Assertions.assertEquals(set1,
                                obj.keySet()
        );
    }

    @Test
    public void test_creates_object_from_pairs() {

        JsObj obj = JsObj.of(JsPair.of(fromKey("a"),
                                       JsInt.of(1)
                             ),
                             JsPair.of(fromKey("b"),
                                       JsInt.of(2)
                             ),
                             JsPair.of(fromKey("c"),
                                       JsInt.of(3)
                             ),
                             JsPair.of(path("/d/0/0"),
                                       JsInt.of(5)
                             ),
                             JsPair.of(path("/d/0/1"),
                                       JsInt.of(6)
                             )
        );

        Assertions.assertEquals(5,
                                obj.sizeAll()
        );

        Assertions.assertEquals(4,
                                obj.size()
        );

        Assertions.assertTrue(
                obj.getInt(path("/d/0/1"))
                   .equals(6)
        );


    }

    @Test
    public void test_creates_three_elements_object() {
        JsObj obj = JsObj.of("a",
                             JsLong.of(10),
                             "b",
                             JsStr.of("b"),
                             "c",
                             JsInt.of(10)
        );
        Assertions.assertEquals(3,
                                obj.size()
        );

        final JsObj obj1 = obj.delete("a");
        Assertions.assertEquals(3,
                                obj.size()
        );
        Assertions.assertEquals(2,
                                obj1.size()
        );

    }

    @Test
    public void test_creates_two_elements_object() {
        JsObj obj = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsStr.of("a")
        );
        Assertions.assertEquals(2,
                                obj.size()
        );

        final JsObj obj1 = obj.delete("b");// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
        );
        Assertions.assertEquals(1,
                                obj1.size()
        );

    }

    @Test
    public void test_equals() {

        JsObj obj = JsObj.of("a",
                             JsObj.of("b",
                                      JsArray.of(1,
                                                 2,
                                                 3
                                      )
                             ),
                             "b",
                             JsArray.of("a",
                                        "b",
                                        "c"
                             )
        );

        JsObj obj2 = JsObj.of("a",
                              JsObj.of("b",
                                       JsArray.of(1,
                                                  2,
                                                  3,
                                                  1,
                                                  2,
                                                  3
                                       )
                              ),
                              "b",
                              JsArray.of("a",
                                         "b",
                                         "c",
                                         "a",
                                         "b",
                                         "c"
                              )
        );
        JsObj obj3 = JsObj.of("a",
                              JsObj.of("b",
                                       JsArray.of(3,
                                                  2,
                                                  1,
                                                  1,
                                                  2,
                                                  3
                                       )
                              ),
                              "b",
                              JsArray.of("c",
                                         "b",
                                         "a",
                                         "a",
                                         "b",
                                         "c"
                              )
        );

        Assertions.assertTrue(obj.equals(obj2,
                                         SET
                              )
        );

    }

    @Test
    public void test_equals_and_hashcode() {
        final JsObj obj = JsObj.of("a",
                                   JsInt.of(1),
                                   "b",
                                   JsBigInt.of(BigInteger.ONE),
                                   "c",
                                   JsLong.of(1),
                                   "d",
                                   JsBigDec.of(BigDecimal.ONE)
        );
        final JsObj obj1 = JsObj.of("a",
                                    JsBigDec.of(BigDecimal.ONE),
                                    "b",
                                    JsLong.of(1),
                                    "c",
                                    JsInt.of(1),
                                    "d",
                                    JsBigInt.of(BigInteger.ONE)
        );

        Assertions.assertEquals(obj,
                                obj1
        );
        Assertions.assertEquals(obj.hashCode(),
                                obj1.hashCode()
        );

    }

    @Test
    public void test_filter_elements_immutable() {

        final JsObj obj = JsObj.parse("{\n"
                                              + "  \"a\": 1,\n"
                                              + "  \"b\": 2,\n"
                                              + "  \"c\": [{\"d\": 3,\"e\": 4}, 5,6]\n"
                                              +
                                              "}");

        final JsObj result = obj.filterValues((path, val) -> val.toJsInt().value % 2 == 0);

        final JsObj result_ = obj.filterAllValues(val -> val.toJsInt().value % 2 == 0);


        Assertions.assertEquals(JsObj.parse("{\"b\":2,\"c\":[{\"e\":4,\"d\":3},5,6]}"),
                                result
        );

        Assertions.assertEquals(JsObj.parse("{\"b\":2,\"c\":[{\"e\":4},6]}"),
                                result_
        );


    }

    @Test
    public void test_filter_jsons() {
        final JsObj obj = JsObj.of("a",
                                   JsObj.of("R",
                                            JsInt.of(1)
                                   ),
                                   "b",
                                   JsObj.of("R",
                                            JsInt.of(1)
                                   ),
                                   "c",
                                   JsArray.of(JsObj.of("R",
                                                       JsInt.of(1)
                                              ),
                                              JsObj.of("S",
                                                       JsInt.of(1),
                                                       "T",
                                                       JsObj.of("R",
                                                                JsInt.of(1)
                                                       )
                                              )
                                   ),
                                   "d",
                                   JsObj.of("d",
                                            JsInt.of(3)
                                   ),
                                   "e",
                                   NULL
        );
        final JsObj result = obj.filterObjs(o -> !o.containsKey("R"));
        final JsObj result1 = obj.filterAllObjs(o -> !o.containsKey("R"));


        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"R\":1},{\"T\":{\"R\":1},\"S\":1}],\"d\":{\"d\":3},\"e\": null}"),
                                result
        );
        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"S\":1}],\"d\":{\"d\":3},\"e\": null}"),
                                result1
        );
    }

    @Test
    public void test_filter_jsons_with_path() {
        final JsObj obj = JsObj.of("a",
                                   JsObj.of("R",
                                            JsInt.of(1)
                                   ),
                                   "b",
                                   JsObj.of("R",
                                            JsInt.of(1)
                                   ),
                                   "c",
                                   JsArray.of(JsObj.of("R",
                                                       JsInt.of(1)
                                              ),
                                              JsObj.of("S",
                                                       JsInt.of(1),
                                                       "T",
                                                       JsObj.of("R",
                                                                JsInt.of(1)
                                                       )
                                              )
                                   ),
                                   "d",
                                   JsObj.of("d",
                                            JsInt.of(3)
                                   ),
                                   "e",
                                   NULL
        );
        final JsObj result = obj.filterObjs((p, o) ->
                                            {
                                                Assertions.assertEquals(o,
                                                                        obj.get(p)
                                                );
                                                return !o.containsKey("R");
                                            });
        final JsObj result1 = obj.filterAllObjs((p, o) ->
                                                {
                                                    Assertions.assertEquals(o,
                                                                            obj.get(p)
                                                    );
                                                    return !o.containsKey("R");
                                                });


        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"R\":1},{\"T\":{\"R\":1},\"S\":1}],\"d\":{\"d\":3},\"e\": null}"),
                                result
        );
        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"S\":1}],\"d\":{\"d\":3},\"e\": null}"),
                                result1
        );
    }

    @Test
    public void test_filter_keys() {
        final Supplier<JsObj> obj = () -> JsObj.of("a",
                                                   JsObj.of("a1",
                                                            JsStr.of("A"),
                                                            "b1",
                                                            JsStr.of("B")
                                                   ),
                                                   "b2",
                                                   JsStr.of("C"),
                                                   "d",
                                                   JsObj.of("d1",
                                                            JsStr.of("D"),
                                                            "e1",
                                                            JsArray.of(JsObj.of("f",
                                                                                JsStr.of("F1")
                                                                       ),
                                                                       TRUE,
                                                                       JsObj.of("b3",
                                                                                JsStr.of("G1"),
                                                                                "c",
                                                                                JsStr.of("G2")
                                                                       ),
                                                                       FALSE
                                                            )
                                                   ),
                                                   "b4",
                                                   JsStr.of("F")
        );


        final JsObj obj1 = obj.get()
                              .filterAllKeys(key -> !key.startsWith("b"));
        Assertions.assertFalse(obj1.streamAll()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))));

        final JsObj obj2 = obj.get()
                              .filterKeys(key -> !key.startsWith("b"));
        Assertions.assertFalse(obj2.stream()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))
                                   ));


        final JsObj obj3 = obj.get()
                              .filterKeys(key ->
                                                  !key.startsWith("b")
                              );
        Assertions.assertFalse(obj3.stream()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))
                                   ));


    }

    @Test
    public void test_filter_keys_with_path() {
        final Supplier<JsObj> obj = () -> JsObj.of("a",
                                                   JsObj.of("a1",
                                                            JsStr.of("A"),
                                                            "b1",
                                                            JsStr.of("B")
                                                   ),
                                                   "b2",
                                                   JsStr.of("C"),
                                                   "d",
                                                   JsObj.of("d1",
                                                            JsStr.of("D"),
                                                            "e1",
                                                            JsArray.of(JsObj.of("f",
                                                                                JsStr.of("F1")
                                                                       ),
                                                                       TRUE,
                                                                       JsObj.of("b3",
                                                                                JsStr.of("G1"),
                                                                                "c",
                                                                                JsStr.of("G2")
                                                                       ),
                                                                       FALSE
                                                            )
                                                   ),
                                                   "b4",
                                                   JsStr.of("F")
        );


        final JsObj obj1 = obj.get()
                              .filterAllKeys((path, val) ->
                                             {
                                                 Assertions.assertEquals(val,
                                                                         obj.get()
                                                                            .get(path)
                                                 );
                                                 return !path.last()
                                                             .isKey(name -> name.startsWith("b"));
                                             }
                              );
        Assertions.assertFalse(obj1.streamAll()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))));

        final JsObj obj2 = obj.get()
                              .filterKeys((key, val) ->
                                          {
                                              Assertions.assertEquals(val,
                                                                      obj.get()
                                                                         .get(key)
                                              );
                                              return !key.startsWith("b");
                                          }
                              );
        Assertions.assertFalse(obj2.stream()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))
                                   ));


        final JsObj obj3 = obj.get()
                              .filterKeys(key ->
                                                  !key.startsWith("b")
                              );
        Assertions.assertFalse(obj3.stream()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))
                                   ));


    }

    @Test
    public void test_filter_values_from_object() {

        JsObj obj = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             NULL,
                             "c",
                             JsArray.of(JsInt.of(1),
                                        NULL,
                                        JsInt.of(2),
                                        NULL,
                                        JsInt.of(3),
                                        JsArray.of(JsInt.of(1),
                                                   NULL,
                                                   JsInt.of(2)
                                        ),
                                        JsObj.of("a",
                                                 NULL,
                                                 "b",
                                                 JsInt.of(1)
                                        )
                             )
        );

        final JsObj obj1 = obj.filterAllValues((path, val) -> val.isNotNull());

        Assertions.assertEquals(JsObj.parse("{\"a\":1,\"c\":[1,2,3,[1,2],{\"b\":1}]}\n"),
                                obj1
        );

    }

    @Test
    public void test_malformed_json() {
        final MalformedJson malformedJson = Assertions.assertThrows(MalformedJson.class,
                                                                    () -> JsObj.parse("")
        );

        Assertions.assertEquals("Expected a json object {...}. Received: ",
                                malformedJson.getMessage()
        );


        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{]")
        );


    }

    @Test
    public void test_map_elements_immutable() {

        final JsObj obj = JsObj.parse("{\n"
                                              + "  \"a\": 1,\n"
                                              + "  \"b\": 2,\n"
                                              + "  \"c\": [{\"d\": 3,\"e\": 4}, 5,6]\n"
                                              +
                                              "}");

        final JsObj result = obj.mapValues(val -> val.toJsInt()
                                                     .map(i -> i + 10));

        final JsObj result_ = obj.mapAllValues(val -> val.toJsInt()
                                                         .map(i -> i + 10));

        Assertions.assertEquals(JsObj.parse("{\"a\":11,\"b\":12,\"c\":[{\"e\":4,\"d\":3},5,6]}\n"),
                                result
        );

        Assertions.assertEquals(JsObj.parse("{\"a\":11,\"b\":12,\"c\":[{\"e\":14,\"d\":13},15,16]}\n"),
                                result_
        );


    }

    @Test
    public void test_map_json_obj() {
        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsObj.empty(),
                             "c",
                             JsArray.empty(),
                             "h",
                             JsArray.of(JsObj.of("c",
                                                 JsStr.of("C"),
                                                 "d",
                                                 JsStr.of("D")
                                        ),
                                        NULL,
                                        JsObj.of("d",
                                                 JsStr.of("D"),
                                                 "e",
                                                 JsStr.of("E"),
                                                 "f",
                                                 JsObj.of("g",
                                                          JsStr.of("G"),
                                                          "h",
                                                          JsStr.of("H")
                                                 )
                                        )
                             )
        );


        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                ,
                                obj.mapAllObjs(o ->
                                               {


                                                   return o.set("size",
                                                                JsInt.of(o.size())
                                                   );
                                               }
                                )
        );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                ,
                                obj.mapAllObjs(o -> o.set("size",
                                                          JsInt.of(o.size())
                                               )
                                )
        );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"f\":{\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                ,
                                obj.mapObjs(o ->
                                            {
                                                return o.set("size",
                                                             JsInt.of(o.size())
                                                );
                                            }
                                )
        );


    }

    @Test
    public void test_map_json_obj_with_path() {
        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsObj.empty(),
                             "c",
                             JsArray.empty(),
                             "h",
                             JsArray.of(JsObj.of("c",
                                                 JsStr.of("C"),
                                                 "d",
                                                 JsStr.of("D")
                                        ),
                                        NULL,
                                        JsObj.of("d",
                                                 JsStr.of("D"),
                                                 "e",
                                                 JsStr.of("E"),
                                                 "f",
                                                 JsObj.of("g",
                                                          JsStr.of("G"),
                                                          "h",
                                                          JsStr.of("H")
                                                 )
                                        )
                             )
        );


        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                ,
                                obj.mapAllObjs((p, o) ->
                                               {

                                                   Assertions.assertEquals(o,
                                                                           obj.get(p)
                                                   );
                                                   return o.set("size",
                                                                JsInt.of(o.size())
                                                   );
                                               }
                                )
        );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                ,
                                obj.mapAllObjs((p, o) -> {
                                                   Assertions.assertEquals(o,
                                                                           obj.get(p)
                                                   );
                                                   return o.set("size",
                                                                JsInt.of(o.size())
                                                   );
                                               }
                                )
        );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"f\":{\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                ,
                                obj.mapObjs((p, o) -> {
                                                Assertions.assertEquals(o,
                                                                        obj.get(p)
                                                );
                                                return o.set("size",
                                                             JsInt.of(o.size())
                                                );
                                            }
                                )
        );


    }

    @Test
    public void test_map_keys_to_uppercase_removing_trailing_white_spaces_immutable() {
        final JsObj obj = JsObj.of(" a ",
                                   JsObj.of(" a1 ",
                                            JsStr.of("A"),
                                            " b1 ",
                                            JsStr.of("B")
                                   ),
                                   " c ",
                                   JsStr.of("C"),
                                   " d ",
                                   JsObj.of(" d1 ",
                                            JsStr.of("D"),
                                            " e1 ",
                                            JsArray.of(JsObj.of(" f ",
                                                                JsStr.of("F1")
                                                       ),
                                                       JsObj.of(" g ",
                                                                JsStr.of("G1")
                                                       )
                                            )
                                   ),
                                   " f ",
                                   JsStr.of("F")
        );


        Assertions.assertFalse(anyKeyLetterIsLowerCase(obj.mapAllKeys((path, val) ->
                                                                      {
                                                                          Assertions.assertEquals(val,
                                                                                                  obj.get(path)
                                                                          );
                                                                          return path.last()
                                                                                     .asKey().name.trim()
                                                                                                  .toUpperCase();
                                                                      })));


        Assertions.assertFalse(anyKeyLetterIsLowerCase(obj.mapAllKeys(key -> key.trim()
                                                                                .toUpperCase())));

    }

    Boolean anyKeyLetterIsLowerCase(final JsObj mapped) {
        return mapped.streamAll()
                     .map(it ->
                          {
                              final String name = it.path.last()
                                                         .asKey().name;
                              return name.contains(" ") || name.chars()
                                                               .mapToObj(Character::isLowerCase)
                                                               .findAny()
                                                               .orElse(false);
                          })
                     .findFirst()
                     .orElse(false);
    }

    @Test
    public void test_map_strings_to_lowercase_and_reduce() {

        final JsObj obj = JsObj.of("a",

                                   JsObj.of("a1",
                                            JsStr.of("A"),
                                            "b1",
                                            JsStr.of("B")
                                   ),
                                   "n",
                                   JsInt.of(1),
                                   "c",
                                   JsStr.of("C"),
                                   "d",
                                   JsObj.of("d1",
                                            JsStr.of("D"),
                                            "e1",
                                            JsStr.of("E"),
                                            "g",
                                            JsInt.of(2)
                                   ),
                                   "f",
                                   JsStr.of("F")
        );


        final JsObj obj1 = obj.mapAllValues((p, val) ->
                                            {
                                                Assertions.assertEquals(val,
                                                                        obj.get(p)
                                                );
                                                return JsStr.prism.modify.apply(String::toLowerCase)
                                                                         .apply(val);
                                            });

        final Optional<String> reduced_ = obj1.reduceAll(String::concat,
                                                         (p, v) ->
                                                         {
                                                             Assertions.assertEquals(v,
                                                                                     obj1.get(p)
                                                             );
                                                             return v.toJsStr().value;
                                                         },
                                                         (p, v) -> v.isStr()
        );

        final char[] chars_ = reduced_.get()
                                      .toCharArray();
        Arrays.sort(chars_);
        Assertions.assertEquals("abcdef",
                                new String(chars_)
        );

        final Optional<String> reduced = obj1.reduce(String::concat,
                                                     (p, v) ->
                                                     {
                                                         Assertions.assertEquals(v,
                                                                                 obj1.get(p)
                                                         );
                                                         return v.toJsStr().value;
                                                     },
                                                     (p, v) -> v.isStr()
        );

        final char[] chars = reduced.get()
                                    .toCharArray();
        Arrays.sort(chars);
        Assertions.assertEquals("cf",
                                new String(chars)
        );
    }

    @Test
    public void test_map_strings_to_lowercase_and_reduce_without_path() {

        final JsObj obj = JsObj.of("a",

                                   JsObj.of("a1",
                                            JsStr.of("A"),
                                            "b1",
                                            JsStr.of("B")
                                   ),
                                   "n",
                                   JsInt.of(1),
                                   "c",
                                   JsStr.of("C"),
                                   "d",
                                   JsObj.of("d1",
                                            JsStr.of("D"),
                                            "e1",
                                            JsStr.of("E"),
                                            "g",
                                            JsInt.of(2)
                                   ),
                                   "f",
                                   JsStr.of("F")
        );


        final JsObj obj1 = obj.mapAllValues(val ->
                                            {
                                                return JsStr.prism.modify.apply(String::toLowerCase)
                                                                         .apply(val);
                                            });

        final Optional<String> reduced_ = obj1.reduceAll(String::concat,
                                                         v -> v.toJsStr().value,
                                                         JsValue::isStr
        );

        final char[] chars_ = reduced_.get()
                                      .toCharArray();
        Arrays.sort(chars_);
        Assertions.assertEquals("abcdef",
                                new String(chars_)
        );

        final Optional<String> reduced = obj1.reduce(String::concat,
                                                     v -> v.toJsStr().value,
                                                     JsValue::isStr
        );

        final char[] chars = reduced.get()
                                    .toCharArray();
        Arrays.sort(chars);
        Assertions.assertEquals("cf",
                                new String(chars)
        );
    }

    @Test
    public void test_parse_into_immutable() {
        JsObj obj = JsObj.of(JsPair.of(path("/a/b/0"),
                                       NULL
                             ),
                             JsPair.of(path("/a/b/1"),
                                       TRUE
                             ),
                             JsPair.of(path("/a/b/c"),
                                       FALSE
                             ),
                             JsPair.of(path("/a/b/c/d"),
                                       JsInt.of(1)
                             ),
                             JsPair.of(path("/a/a/a/"),
                                       JsStr.of("a")
                             ),
                             JsPair.of(path("/a/b/0"),
                                       JsBigDec.of(BigDecimal.ONE)
                             )
        );

        Assertions.assertEquals(obj,
                                JsObj.parse(obj.toString())

        );

        Assertions.assertEquals(obj,
                                JsObj.parse(obj.toString())

        );

    }


    @Test
    public void test_set_and_get() {
        final JsObj empty = JsObj.empty();
        final JsObj a = empty.set("a",
                                  JsBigDec.of(BigDecimal.valueOf(0.1d))
        );

        Assertions.assertTrue(
                a.getDouble(fromKey("a"))
                 .equals(0.1d)
        );
        Assertions.assertTrue(
                a.getBigDec(fromKey("a"))
                 .equals(BigDecimal.valueOf(0.1d))
        );
    }

    @Test
    public void test_set_and_get_with_padding() {
        final JsObj empty = JsObj.empty();
        JsPath path = path("/a/b/2");
        final JsObj a = empty.set(path,
                                  JsBigDec.of(BigDecimal.valueOf(0.1d)),
                                  JsDouble.of(0.0)
        );

        Assertions.assertTrue(
                a.getDouble(path)
                 .equals(0.1d)
        );
        Assertions.assertTrue(
                a.getDouble(path("/a/b/0"))
                 .equals(0.0d)
        );
        Assertions.assertTrue(
                a.getDouble(path("/a/b/1"))
                 .equals(0.0d)
        );

    }

    @Test
    public void testJsObjFromIterable() {

        Map<String, JsValue> map = new HashMap<>();

        JsObj a = JsObj.ofIterable(map.entrySet());

        Assertions.assertEquals(JsObj.empty(),
                                a
        );

        map.put("a",
                JsInt.of(1)
        );
        map.put("b",
                JsStr.of("hi")
        );

        Assertions.assertEquals(
                JsObj.ofIterable(map.entrySet()),
                JsObj.of("a",
                         JsInt.of(1),
                         "b",
                         JsStr.of("hi")
                )
        );

    }

    @Test
    public void testJsObj() {
        Instant now = Instant.now();
        byte[] bytes = "hola".getBytes();
        JsObj o = JsObj.of("a",
                           JsBigDec.of(BigDecimal.valueOf(1.5)),
                           "b",
                           JsBigInt.of(BigInteger.valueOf(Long.MAX_VALUE)),
                           "c",
                           JsDouble.of(1.5d),
                           "d",
                           JsLong.of(15L),
                           "e",
                           JsInstant.of(now),
                           "f",
                           JsBinary.of(bytes)
        );

        Assertions.assertNull(o.getStr("b"));
        Assertions.assertEquals("a",o.getStr("bye",()->"a"));
        Assertions.assertNull(o.getInt("b"));
        Assertions.assertEquals(1,
                                (int) o.getInt("b",
                                               () -> 1));
        Assertions.assertNull(o.getBool("b"));
        Assertions.assertFalse(o.getBool("b",()->false));

        Assertions.assertNull(o.getObj("b"));
        Assertions.assertEquals(JsObj.empty(),o.getObj("b",()->JsObj.empty()));

        Assertions.assertNull(o.getDouble("b"));
        Assertions.assertEquals(1.5,(double)o.getDouble("b",()->1.5));

        Assertions.assertNull(o.getArray("b"));
        Assertions.assertEquals(JsArray.empty(),o.getArray("b",()->JsArray.empty()));

        Assertions.assertNull(o.getLong("c"));
        Assertions.assertEquals(10L,(long)o.getLong("c",()->10L));

        Assertions.assertNull(o.getStr("e"));
        Assertions.assertEquals("hi",o.getStr("e",()->"hi"));

        Assertions.assertEquals(BigDecimal.valueOf(1.5),
                                o.getBigDec("a")
        );
        Assertions.assertEquals(BigDecimal.ONE,
                                o.getBigDec("hi",()->BigDecimal.ONE)
        );
        Assertions.assertEquals(BigInteger.valueOf(Long.MAX_VALUE),
                                o.getBigInt("b")
        );
        Assertions.assertEquals(BigInteger.TEN,
                                o.getBigInt("bye",()->BigInteger.TEN)
        );
        Assertions.assertEquals(Double.valueOf(1.5),
                                o.getDouble("c")
        );
        Assertions.assertEquals(Long.valueOf(15L),
                                o.getLong("d")
        );

        Assertions.assertEquals(now,
                                o.getInstant("e")
        );

        Assertions.assertEquals(Instant.MAX,
                                o.getInstant("bye",
                                             () -> Instant.MAX)
        );

        Assertions.assertTrue(
                Arrays.equals(o.getBinary("f"),
                              "hola".getBytes()
                )
        );

        JsObj parsed = JsObj.parse(o.toString());

        Assertions.assertEquals(parsed,
                                o
        );

        Assertions.assertEquals(parsed.getStr("f"),
                                Base64.getEncoder()
                                      .encodeToString("hola".getBytes())
        );


        Option<JsObj, byte[]> fOpt = JsObj.lens.str("f")
                                               .compose(JsStr.base64Prism);

        Option<JsObj, Instant> eOpt = JsObj.lens.str("e")
                                                .compose(JsStr.instantPrism);

        Assertions.assertTrue(Arrays.equals(fOpt.get.apply(parsed)
                                                    .get(),
                                            "hola".getBytes()
        ));


        Assertions.assertTrue(!fOpt.get.apply(JsObj.of("f",
                                                       JsStr.of("a")
                                   ))
                                       .isPresent());


        Assertions.assertEquals(eOpt.get.apply(parsed)
                                        .get(),
                                now
        );

        Assertions.assertTrue(!eOpt.get.apply(JsObj.of("e",
                                                       JsStr.of(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()))
                                              )
                                   )
                                       .isPresent());
    }

    @Test
    public void testGetMethods() {

        JsObj a = JsObj.of("a",
                           JsInt.of(1),
                           "b",
                           JsStr.of("hi"),
                           "c",
                           JsBool.TRUE,
                           "d",
                           JsLong.of(1),
                           "e",
                           JsDouble.of(1.5),
                           "f",
                           JsBigInt.of(BigInteger.TEN),
                           "g",
                           JsBigDec.of(BigDecimal.TEN),
                           "h",
                           JsArray.of(JsStr.of("bye"),
                                      JsInt.of(1),
                                      JsBool.FALSE,
                                      JsLong.of(1L)
                           ),
                           "i",
                           JsObj.of("a",
                                    JsInt.of(1),
                                    "b",
                                    JsArray.of(1,
                                               2,
                                               3
                                    ),
                                    "c",
                                    JsObj.empty()
                           )
        );

        Assertions.assertTrue(1 == a.getInt("a"));
        Assertions.assertEquals("hi",
                                a.getStr("b")
        );
        Assertions.assertEquals(true,
                                a.getBool("c")
        );
        Assertions.assertTrue(1L == a.getLong("d"));
        Assertions.assertTrue(1.5 == a.getDouble("e"));
        Assertions.assertEquals(BigInteger.TEN,
                                a.getBigInt("f")
        );
        Assertions.assertEquals(BigDecimal.TEN,
                                a.getBigDec("g")
        );
        Assertions.assertEquals(JsArray.of(1,
                                           2,
                                           3
                                ),
                                a.getArray(JsPath.path("/i/b"))
        );

        Assertions.assertEquals(JsObj.empty(),
                                a.getObj(JsPath.path("/i/c"))
        );

        Assertions.assertNull(a.getInt("b"));
        Assertions.assertNull(a.getLong("b"));
        Assertions.assertNull(a.getBigDec("b"));
        Assertions.assertNull(a.getBigInt("b"));
        Assertions.assertNull(a.getBool("b"));
        Assertions.assertNull(a.getObj("b"));
        Assertions.assertNull(a.getArray("b"));

        Assertions.assertNull(
                a.getInt("b")
        );
        Assertions.assertEquals(null,
                                a.getLong("b")
        );
        Assertions.assertNull(
                a.getBigDec("b")
        );
        Assertions.assertNull(a.getBigInt("b")
        );
        Assertions.assertNull(
                a.getBool("b")
        );
        Assertions.assertNull(
                a.getObj("b")
        );
        Assertions.assertNull(
                a.getArray("b"));

    }
}
