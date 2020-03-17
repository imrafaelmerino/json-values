package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsPath.fromKey;
import static jsonvalues.JsPath.path;

public class TestJsObj
{

    @Test
    public void test_append_immutable()
    {
        JsObj empty = JsObj.empty();

        final JsObj obj = empty.append(path("/a/b"),
                                       JsStr.of("hi")
                                      );

        Assertions.assertTrue(empty.isEmpty());

        Assertions.assertEquals(JsArray.of("hi"),
                                obj.getArrayOpt(path("/a/b"))
                                   .get()
                               );

        final JsObj obj1 = obj.append(path("/a/b"),
                                      JsStr.of("bye")
                                     );

        Assertions.assertEquals(Optional.of("bye"),
                                obj1.getStrOpt(path("/a/b/-1"))
                               );

        Assertions.assertEquals(Optional.of("hi"),
                                obj1.getStrOpt(path("/a/b/0"))
                               );
    }

    @Test
    public void test_creates_immutable_empty_object()
    {
        JsObj obj = JsObj.empty();
        Assertions.assertTrue(obj.isEmpty());

        final JsObj obj1 = obj.put(JsPath.fromKey("a"),
                                   1
                                  );// obj is mutable
        Assertions.assertTrue(obj.isEmpty());
        Assertions.assertEquals(OptionalInt.of(1),
                                obj1.getIntOpt(JsPath.fromKey("a"))
                               );
    }

    @Test
    public void test_creates_immutable_five_elements_object()
    {

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
        final JsObj obj1 = obj.mapKeys(it -> it.path.last()
                                                    .asKey().name.toUpperCase());

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
    public void test_creates_immutable_four_elements_object()
    {
        JsObj obj = JsObj.of("a",
                             JsStr.of("A"),
                             "b",
                             JsStr.of("B"),
                             "c",
                             JsStr.of("C"),
                             "h",
                             NULL,
                             "d",
                             JsStr.of("D")
                            );
        Assertions.assertEquals(5,
                                obj.size()
                               );

        final JsObj obj1 = obj.mapKeys(pair -> pair.path.last()
                                                        .asKey().name + pair.value.toJsStr().value,
                                       p -> p.value.isStr()
                                      );// obj is mutable

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("aA",
                                 "bB",
                                 "cC",
                                 "h",
                                 "dD"
                                ));
        Assertions.assertEquals(set,
                                obj1.keySet()

                               );

        Set<String> set1 = new HashSet<>();
        set1.addAll(Arrays.asList("a",
                                  "b",
                                  "c",
                                  "d",
                                  "h"
                                 ));
        Assertions.assertEquals(set1,
                                obj.keySet()

                               );

    }

    @Test
    public void test_creates_immutable_object_from_pairs()
    {

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

        Assertions.assertEquals(OptionalInt.of(6),
                                obj.getIntOpt(path("/d/0/1"))
                               );


    }

    @Test
    public void test_creates_immutable_three_elements_object()
    {
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

        final JsObj obj1 = obj.remove(JsPath.fromKey("a"));// obj is mutable
        Assertions.assertEquals(3,
                                obj.size()
                               );
        Assertions.assertEquals(2,
                                obj1.size()
                               );

    }

    @Test
    public void test_creates_immutable_two_elements_object()
    {
        JsObj obj = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsStr.of("a")
                            );
        Assertions.assertEquals(2,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove(JsPath.fromKey("b"));// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
                               );
        Assertions.assertEquals(1,
                                obj1.size()
                               );

    }

    @Test
    public void test_equals()
    {

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
        Assertions.assertTrue(obj.equals(obj,
                                         LIST
                                        )
                             );

        Assertions.assertFalse(obj.equals(obj2,
                                          LIST
                                         )
                              );
        Assertions.assertTrue(obj2.equals(obj3,
                                          SET
                                         )
                             );
        Assertions.assertTrue(obj2.equals(obj3,
                                          MULTISET
                                         )
                             );
        Assertions.assertTrue(obj.equals(obj2,
                                         SET
                                        )
                             );
        Assertions.assertTrue(obj2.equals(obj2,
                                          LIST
                                         )
                             );
        Assertions.assertTrue(obj3.equals(obj3,
                                          LIST
                                         )
                             );
    }

    @Test
    public void test_equals_and_hashcode()
    {
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
    public void test_filter_elements_immutable() throws MalformedJson
    {

        final JsObj obj = JsObj.parse("{\n"
                                      + "  \"a\": 1,\n"
                                      + "  \"b\": 2,\n"
                                      + "  \"c\": [{\"d\": 3,\"e\": 4}, 5,6]\n"
                                      +
                                      "}");

        final JsObj result = obj.filterValues((pair) -> pair.value.toJsInt().value % 2 == 0);

        final JsObj result_ = obj.filterAllValues((pair) -> pair.value.toJsInt().value % 2 == 0);


        Assertions.assertEquals(JsObj.parse("{\"b\":2,\"c\":[{\"e\":4,\"d\":3},5,6]}"),
                                result
                               );

        Assertions.assertEquals(JsObj.parse("{\"b\":2,\"c\":[{\"e\":4},6]}"),
                                result_
                               );


    }

    @Test
    public void test_filter_jsons_from_immutable() throws MalformedJson
    {
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
                                                return !o.containsPath(JsPath.fromKey("R"));
                                            });
        final JsObj result1 = obj.filterAllObjs((p, o) ->
                                              {
                                                  Assertions.assertEquals(o,
                                                                          obj.get(p)
                                                                         );
                                                  return !o.containsPath(JsPath.fromKey("R"));
                                              });


        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"R\":1},{\"T\":{\"R\":1},\"S\":1}],\"d\":{\"d\":3},\"e\": null}"),
                                result
                               );
        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"S\":1}],\"d\":{\"d\":3},\"e\": null}"),
                                result1
                               );
    }

    @Test
    public void test_filter_keys_immutable()
    {
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
                              .filterAllKeys(p ->
                                           {
                                               Assertions.assertEquals(p.value,
                                                                       obj.get()
                                                                          .get(p.path)
                                                                      );
                                               return !p.path.last()
                                                             .isKey(name -> name.startsWith("b"));
                                           }
                                            );
        Assertions.assertFalse(obj1.streamAll()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))));

        final JsObj obj2 = obj.get()
                              .filterKeys(p ->
                                          {
                                              Assertions.assertEquals(p.value,
                                                                      obj.get()
                                                                         .get(p.path)
                                                                     );
                                              return !p.path.last()
                                                            .isKey(name -> name.startsWith("b"));
                                          }
                                         );
        Assertions.assertFalse(obj2.stream()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))
                                            ));


    }

    @Test
    public void test_filter_values_from_immutable_object() throws MalformedJson
    {

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

        final JsObj obj1 = obj.filterAllValues(p -> p.value.isNotNull());

        Assertions.assertEquals(JsObj.parse("{\"a\":1,\"c\":[1,2,3,[1,2],{\"b\":1}]}\n"),
                                obj1
                               );

    }

    @Test
    public void test_malformed_json() throws MalformedJson
    {
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
    public void test_map_elements_immutable() throws MalformedJson
    {

        final JsObj obj = JsObj.parse("{\n"
                                      + "  \"a\": 1,\n"
                                      + "  \"b\": 2,\n"
                                      + "  \"c\": [{\"d\": 3,\"e\": 4}, 5,6]\n"
                                      +
                                      "}");

        final JsObj result = obj.mapValues((pair) -> pair.value.toJsInt()
                                                               .map(i -> i + 10));

        final JsObj result_ = obj.mapAllValues((pair) -> pair.value.toJsInt()
                                                                   .map(i -> i + 10));

        Assertions.assertEquals(JsObj.parse("{\"a\":11,\"b\":12,\"c\":[{\"e\":4,\"d\":3},5,6]}\n"),
                                result
                               );

        Assertions.assertEquals(JsObj.parse("{\"a\":11,\"b\":12,\"c\":[{\"e\":14,\"d\":13},15,16]}\n"),
                                result_
                               );


    }

    @Test
    public void test_map_json_immutable_obj() throws MalformedJson
    {
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


        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put(JsPath.fromKey("size"),
                                                                                    json.size()
                                                                                   );
        final JsObj newObj = obj.mapAllObjs((p, o) ->
                                          {
                                              Assertions.assertEquals(o,
                                                                      obj.get(p)
                                                                     );
                                              return addSizeFn.apply(p,
                                                                     o
                                                                    );
                                          },
                                            (p, o) -> o.isNotEmpty()
                                           );

        Assertions.assertNotEquals(obj,
                                   newObj
                                  );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}\n")
        ,
                                newObj
                               );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
        ,
                                obj.mapAllObjs((p, o) ->
                                             {

                                                 Assertions.assertEquals(o,
                                                                         obj.get(p)
                                                                        );
                                                 return addSizeFn.apply(p,
                                                                        o
                                                                       );
                                             }
                                              )
                               );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"f\":{\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
        ,
                                obj.mapObjs((p, o) ->
                                            {

                                                Assertions.assertEquals(o,
                                                                        obj.get(p)
                                                                       );
                                                return addSizeFn.apply(p,
                                                                       o
                                                                      );
                                            }
                                           )
                               );

        JsObj obj1 = JsObj.of("a",
                              JsObj.of("b",
                                       JsStr.of("B"),
                                       "c",
                                       JsStr.of("C")
                                      ),
                              "b",
                              JsObj.empty(),
                              "c",
                              JsArray.empty(),
                              "d",
                              JsObj.of("e",
                                       JsStr.of("E"),
                                       "f",
                                       JsStr.of("F"),
                                       "g",
                                       JsObj.of("h",
                                                JsStr.of("H"),
                                                "i",
                                                JsStr.of("I")
                                               )
                                      )
                             );

        final JsObj newObj1 = obj1.mapObjs((p, o) ->
                                           {
                                               Assertions.assertEquals(o,
                                                                       obj1.get(p)
                                                                      );
                                               return addSizeFn.apply(p,
                                                                      o
                                                                     );
                                           },
                                           (p, o) -> o.isNotEmpty()
                                          );


        Assertions.assertEquals(JsObj.parse("{\"a\":{\"size\":2,\"b\":\"B\",\"c\":\"C\"},\"b\":{},\"c\":[],\"d\":{\"e\":\"E\",\"size\":3,\"f\":\"F\",\"g\":{\"i\":\"I\",\"h\":\"H\"}}}\n")
        ,
                                newObj1
                               );

    }

    @Test
    public void test_map_keys_to_uppercase_removing_trailing_white_spaces_immutable()
    {
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


        final JsObj mapped = obj.mapAllKeys(p ->
                                          {
                                              Assertions.assertEquals(p.value,
                                                                      obj.get(p.path)
                                                                     );
                                              return p.path.last()
                                                           .asKey().name.trim()
                                                                        .toUpperCase();
                                          });

        Assertions.assertFalse(mapped.streamAll()
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
                                     .orElse(false));

    }

    @Test
    public void test_map_strings_to_lowercase_and_reduce()
    {

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


        final JsObj obj1 = obj.mapAllValues(pair ->
                                         {
                                             Assertions.assertEquals(pair.value,
                                                                     obj.get(pair.path)
                                                                    );
                                             return pair.mapIfStr(String::toLowerCase).value;
                                         });

        final Optional<String> reduced_ = obj1.reduceAll(String::concat,
                                                       p ->
                                                       {
                                                           Assertions.assertEquals(p.value,
                                                                                   obj1.get(p.path)
                                                                                  );
                                                           return p.value.toJsStr().value;
                                                       },
                                                       p -> p.value.isStr()
                                                        );

        final char[] chars_ = reduced_.get()
                                      .toCharArray();
        Arrays.sort(chars_);
        Assertions.assertEquals("abcdef",
                                new String(chars_)
                               );

        final Optional<String> reduced = obj1.reduce(String::concat,
                                                     p ->
                                                     {
                                                         Assertions.assertEquals(p.value,
                                                                                 obj1.get(p.path)
                                                                                );
                                                         return p.value.toJsStr().value;
                                                     },
                                                     p -> p.value.isStr()
                                                    );

        final char[] chars = reduced.get()
                                    .toCharArray();
        Arrays.sort(chars);
        Assertions.assertEquals("cf",
                                new String(chars)
                               );
    }

    @Test
    public void test_operations()
    {

        JsObj obj = JsObj.of(JsPair.of(path("/a/b/c"),
                                       JsInt.of(1)
                                      ),
                             JsPair.of(path("/a/b/d"),
                                       JsInt.of(2)
                                      )
                            );

        Assertions.assertEquals(obj.appendAll(JsPath.fromIndex(0),
                                              JsArray.of(1,
                                                         2
                                                        )
                                             ),
                                obj
                               );

        Assertions.assertEquals(obj.prependAll(JsPath.fromIndex(0),
                                               JsArray.of(1,
                                                          2
                                                         )
                                              ),
                                obj
                               );
        Assertions.assertEquals(NOTHING,
                                obj.get(JsPath.fromIndex(0))
                               );
        Assertions.assertEquals(obj,
                                obj.put(JsPath.fromIndex(0),
                                        a -> NULL
                                       )
                               );

        Assertions.assertEquals(obj,
                                obj.remove(path("/a/0"))
                               );

        Assertions.assertEquals(obj,
                                obj.remove(JsPath.fromIndex(0))
                               );

        Assertions.assertEquals(obj,
                                obj.remove(path("/a/b/c/d"))
                               );

        Assertions.assertEquals(obj,
                                obj.remove(path("/a/b/c/0"))
                               );
    }

    @Test
    public void test_parse_into_immutable() throws MalformedJson
    {
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
    public void test_prepend()
    {

        JsObj obj = JsObj.of("a",
                             JsArray.of(TRUE),
                             "b",
                             JsArray.of(FALSE)
                            );

        final JsObj obj1 = obj.prepend(path("/a"),
                                       TRUE
                                      )
                              .append(path("/a"),
                                      FALSE
                                     );
        Assertions.assertEquals(JsArray.of(true,
                                           true,
                                           false
                                          ),
                                obj1.get(path("/a"))
                               );

        Assertions.assertEquals(3,
                                obj1.size(path("/a"))
                                    .getAsInt()
                               );

        final JsObj obj2 = obj.prepend(path("/a/b"),
                                       JsStr.of("hi")
                                      );
        Assertions.assertEquals(JsArray.of("hi"),
                                obj2.get(path("/a/b"))
                               );


        final JsObj obj3 = obj2.prepend(path("/a/b/3"),
                                        JsStr.of("bye")
                                       );
        Assertions.assertEquals(JsArray.of("bye"),
                                obj3
                                .get(path("/a/b/3"))
                               );

        Assertions.assertEquals(JsStr.of("hi"),
                                obj3.get(path("/a/b/0"))
                               );
        Assertions.assertEquals(NULL,
                                obj3.get(path("/a/b/1"))
                               );
        Assertions.assertEquals(NULL,
                                obj3.get(path("/a/b/2"))
                               );

        Assertions.assertEquals(JsArray.of(1),
                                obj3.prepend(path("/a/b/3/1"),
                                             JsInt.of(1)
                                            )
                                    .get(path("/a/b/3/1"))
                               );


    }

    @Test
    public void test_prepend_all_immutable()
    {
        JsObj empty = JsObj.empty();

        final JsArray xs = JsArray.of(1,
                                      2,
                                      3
                                     );
        final JsObj obj = empty.prependAll(path("/a/b"),
                                           xs
                                          );

        Assertions.assertTrue(empty.isEmpty());

        Assertions.assertEquals(xs,
                                obj.get(path("/a/b"))
                               );

        final JsObj obj1 = obj.prependAll(path("/a/b"),
                                          xs
                                         );

        Assertions.assertEquals(xs.prependAll(xs),
                                obj1.get(path("/a/b"))
                               );


        final JsObj obj2 = empty.prependAll(path("/c/2"),
                                            xs
                                           );


        Assertions.assertEquals(JsNull.NULL,
                                obj2.get(path("/c/0"))
                               );

        Assertions.assertEquals(JsNull.NULL,
                                obj2.get(path("/c/1"))
                               );

        Assertions.assertEquals(xs,
                                obj2.get(path("/c/2"))
                               );

        Assertions.assertEquals(xs,
                                empty.prependAll(path("/d/2"),
                                                 xs
                                                )
                                     .get(path("/d/2"))
                               );

        final JsObj obj3 = obj2.prepend(path("/c/1"),
                                        JsInt.of(1)
                                       );

        Assertions.assertEquals(JsInt.of(1),
                                obj3.get(path("/c/1/0"))
                               );
    }

    @Test
    public void test_put_and_get()
    {
        final JsObj empty = JsObj.empty();
        final JsObj a = empty.put(JsPath.fromKey("a"),
                                  JsBigDec.of(BigDecimal.valueOf(0.1d))
                                 );

        Assertions.assertEquals(OptionalDouble.of(0.1d),
                                a.getDoubleOpt(fromKey("a"))
                               );
        Assertions.assertEquals(Optional.of(BigDecimal.valueOf(0.1d)),
                                a.getBigDecimalOpt(fromKey("a"))
                               );
    }



}
