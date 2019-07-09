import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsPath.of;

class TestJsObj
{
    @Test
    void creates_mutable_empty_object()
    {
        JsObj obj = JsObj._empty_();
        Assertions.assertTrue(obj.isEmpty());

        final JsObj obj1 = obj.put("a",
                                   1
                                  );// obj is mutable
        Assertions.assertEquals(1,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );
    }

    @Test
    void creates_immutable_empty_object()
    {
        JsObj obj = JsObj.empty();
        Assertions.assertTrue(obj.isEmpty());

        final JsObj obj1 = obj.put("a",
                                   1
                                  );// obj is mutable
        Assertions.assertTrue(obj.isEmpty());
        Assertions.assertEquals(OptionalInt.of(1),
                                obj1.getInt("a")
                               );
    }

    @Test
    void creates_mutable_one_element_object()
    {
        JsObj obj = JsObj._of_("a",
                               JsInt.of(1)
                              );
        Assertions.assertEquals(1,
                                obj.size()
                               );

        final JsObj obj1 = obj.put("b",
                                   2
                                  );// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );
    }

    @Test
    void creates_mutable_two_elements_object()
    {
        JsObj obj = JsObj._of_("a",
                               JsInt.of(1),
                               "b",
                               JsStr.of("a")
                              );
        Assertions.assertEquals(2,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove("b");// obj is mutable
        Assertions.assertEquals(1,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );

    }

    @Test
    void creates_immutable_two_elements_object()
    {
        JsObj obj = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsStr.of("a")
                            );
        Assertions.assertEquals(2,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove("b");// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
                               );
        Assertions.assertEquals(1,
                                obj1.size()
                               );

    }

    @Test
    void creates_mutable_three_elements_object()
    {
        JsObj obj = JsObj._of_("a",
                               JsLong.of(10),
                               "b",
                               JsStr.of("b"),
                               "c",
                               JsInt.of(10)
                              );
        Assertions.assertEquals(3,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove("a");// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );

    }

    @Test
    void creates_immutable_three_elements_object()
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

        final JsObj obj1 = obj.remove("a");// obj is mutable
        Assertions.assertEquals(3,
                                obj.size()
                               );
        Assertions.assertEquals(2,
                                obj1.size()
                               );

    }

    @Test
    void creates_mutable_four_elements_object()
    {
        JsObj obj = JsObj._of_("a",
                               JsStr.of("A"),
                               "b",
                               JsStr.of("B"),
                               "c",
                               JsStr.of("C"),
                               "d",
                               JsStr.of("D"),
                               "e",
                               JsObj._empty_()
                              );
        Assertions.assertEquals(5,
                                obj.size()
                               );

        obj.mapKeys(pair -> pair.path.last()
                                     .asKey().name + pair.elem.asJsStr().x,
                    p -> p.elem.isStr()
                   ); // obj is mutable

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("aA",
                                 "bB",
                                 "cC",
                                 "dD",
                                 "e"
                                ));
        Assertions.assertEquals(obj.fields(),
                                set
                               );

    }

    @Test
    void creates_immutable_four_elements_object()
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
                                                        .asKey().name + pair.elem.asJsStr().x,
                                       p -> p.elem.isStr()
                                      );// obj is mutable

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("aA",
                                 "bB",
                                 "cC",
                                 "h",
                                 "dD"
                                ));
        Assertions.assertEquals(set,
                                obj1.fields()

                               );

        Set<String> set1 = new HashSet<>();
        set1.addAll(Arrays.asList("a",
                                  "b",
                                  "c",
                                  "d",
                                  "h"
                                 ));
        Assertions.assertEquals(set1,
                                obj.fields()

                               );

    }

    @Test
    void creates_mutable_five_elements_object()
    {

        JsObj obj = JsObj._of_("a",
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
        obj.remove("b");
        Assertions.assertEquals(4,
                                obj.size()
                               );
    }

    @Test
    void creates_immutable_five_elements_object()
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
                                obj1.fields()
                               );
        Set<String> set1 = new HashSet<>();
        set1.addAll(Arrays.asList("a",
                                  "b",
                                  "c",
                                  "d",
                                  "e"
                                 ));
        Assertions.assertEquals(set1,
                                obj.fields()
                               );
    }

    @Test
    void creates_mutable_object_from_pairs()
    {

        JsObj obj = JsObj._of_(JsPair.of("a",
                                         JsInt.of(1)
                                        ),
                               JsPair.of("b",
                                         JsInt.of(2)
                                        ),
                               JsPair.of("c",
                                         JsInt.of(3)
                                        ),
                               JsPair.of("d.0.0",
                                         JsInt.of(5)
                                        ),
                               JsPair.of("d.0.1",
                                         JsInt.of(6)
                                        )
                              );

        Assertions.assertEquals(5,
                                obj.size_()
                               );

        Assertions.assertEquals(4,
                                obj.size()
                               );

        Assertions.assertEquals(OptionalInt.of(6),
                                obj.getInt("d.0.1")
                               );


    }

    @Test
    void creates_immutable_object_from_pairs()
    {

        JsObj obj = JsObj.of(JsPair.of("a",
                                       JsInt.of(1)
                                      ),
                             JsPair.of("b",
                                       JsInt.of(2)
                                      ),
                             JsPair.of("c",
                                       JsInt.of(3)
                                      ),
                             JsPair.of("d.0.0",
                                       JsInt.of(5)
                                      ),
                             JsPair.of("d.0.1",
                                       JsInt.of(6)
                                      )
                            );

        Assertions.assertEquals(5,
                                obj.size_()
                               );

        Assertions.assertEquals(4,
                                obj.size()
                               );

        Assertions.assertEquals(OptionalInt.of(6),
                                obj.getInt("d.0.1")
                               );


    }


    @Test
    void head_and_tail_of_empty_obj_returns_exception()
    {

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsObj.empty()
                                           .head()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsObj._empty_()
                                           .head()
                               );
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsObj.empty()
                                           .tail("a")
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsObj._empty_()
                                           .tail("a")
                               );
    }

    @Test
    void equals_and_hashcode()
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
    void map_strings_to_lowercase_and_reduce()
    {

        final JsObj immutable = JsObj.of("a",
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

        final JsObj mutable = JsObj._of_("a",
                                         JsObj._of_("a1",
                                                    JsStr.of("A"),
                                                    "b1",
                                                    JsStr.of("B")
                                                   ),
                                         "n",
                                         JsInt.of(1),
                                         "c",
                                         JsStr.of("C"),
                                         "d",
                                         JsObj._of_("d1",
                                                    JsStr.of("D"),
                                                    "e1",
                                                    JsStr.of("E")
                                                   ),
                                         "f",
                                         JsStr.of("F")
                                        );

        for (JsObj obj : Arrays.asList(immutable,
                                       mutable
                                      ))
        {

            final JsObj obj1 = obj.mapElems_(pair ->
                                              {
                                                  Assertions.assertEquals(pair.elem,
                                                                          obj.get(pair.path)
                                                                         );
                                                  return Utils.mapIfStr(String::toLowerCase)
                                                              .apply(pair).elem;
                                              });

            final Optional<String> reduced_ = obj1.reduce_(String::concat,
                                                           p ->
                                                           {
                                                               Assertions.assertEquals(p.elem,
                                                                                       obj1.get(p.path)
                                                                                      );
                                                               return p.elem.asJsStr().x;
                                                           },
                                                           p -> p.elem.isStr()
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
                                                             Assertions.assertEquals(p.elem,
                                                                                     obj1.get(p.path)
                                                                                    );
                                                             return p.elem.asJsStr().x;
                                                         },
                                                         p -> p.elem.isStr()
                                                        );

            final char[] chars = reduced.get()
                                        .toCharArray();
            Arrays.sort(chars);
            Assertions.assertEquals("cf",
                                    new String(chars)
                                   );
        }


    }

    @Test
    void map_keys_to_uppercase_removing_trailing_white_spaces_mutable()
    {

        final Supplier<JsObj> supplier = () -> JsObj._of_(" a ",
                                                          JsObj._of_(" a1 ",
                                                                     JsStr.of("A"),
                                                                     " b1 ",
                                                                     JsStr.of("B")
                                                                    ),
                                                          " c ",
                                                          JsStr.of("C"),
                                                          " d ",
                                                          JsObj._of_(" d1 ",
                                                                     JsStr.of("D"),
                                                                     " e1 ",
                                                                     JsArray._of_(JsObj._of_(" f ",
                                                                                             JsStr.of("F1")
                                                                                            ),
                                                                                  JsObj._of_(" g ",
                                                                                             JsStr.of("G1")
                                                                                            )
                                                                                 )
                                                                    ),
                                                          " f ",
                                                          JsStr.of("F")
                                                         );


        final JsObj mapped = supplier.get()
                                     .mapKeys_(p ->
                                               {

                                                   Assertions.assertEquals(p.elem,
                                                                           supplier.get()
                                                                                   .get(p.path)
                                                                          );
                                                   return p.path.last()
                                                                .asKey().name.trim()
                                                                             .toUpperCase();


                                               });

        Assertions.assertFalse(mapped.stream_()
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
    void map_keys_to_uppercase_removing_trailing_white_spaces_immutable()
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


        final JsObj mapped = obj.mapKeys_(p ->
                                          {
                                              Assertions.assertEquals(p.elem,
                                                                      obj.get(p.path)
                                                                     );
                                              return p.path.last()
                                                           .asKey().name.trim()
                                                                        .toUpperCase();
                                          });

        Assertions.assertFalse(mapped.stream_()
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
    void map_keys_to_uppercase_removing_trailing_white_spaces_if_condition()
    {
        final JsObj immutable = JsObj.of(" a ",
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

        final JsObj mutable = JsObj._of_(" a ",
                                         JsObj._of_(" a1 ",
                                                    JsStr.of("A"),
                                                    " b1 ",
                                                    JsStr.of("B")
                                                   ),
                                         " c ",
                                         JsStr.of("C"),
                                         " d ",
                                         JsObj._of_(" d1 ",
                                                    JsStr.of("D"),
                                                    " e1 ",
                                                    JsArray._of_(JsObj._of_(" f ",
                                                                            JsStr.of("F1")
                                                                           ),
                                                                 JsObj._of_(" g ",
                                                                            JsStr.of("G1")
                                                                           )
                                                                )
                                                   ),
                                         " f ",
                                         JsStr.of("F")
                                        );


        for (JsObj obj : Arrays.asList(mutable,
                                       immutable
                                      ))
        {
            final Predicate<JsPair> containsLetterD = p -> p.path.last()
                                                                 .isKey(name -> name.contains("d"));
            final JsObj mapped = obj.mapKeys_(p ->
                                              {
                                                  Assertions.assertEquals(p.elem,
                                                                          obj.get(p.path)
                                                                         );
                                                  return p.path.last()
                                                               .asKey().name.trim()
                                                                            .toUpperCase();
                                              },
                                              containsLetterD.negate()
                                             );

            Assertions.assertFalse(mapped.stream_()
                                         .filter(containsLetterD.negate())
                                         .anyMatch(it ->
                                                   {
                                                       final String name = it.path.last()
                                                                                  .asKey().name;
                                                       return name.contains(" ") || name.chars()
                                                                                        .mapToObj(Character::isLowerCase)
                                                                                        .findAny()
                                                                                        .orElse(false);
                                                   }));

        }
    }


    @Test
    void filter_keys_immutable()
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
                              .filterKeys_(p ->
                                           {
                                               Assertions.assertEquals(p.elem,
                                                                       obj.get()
                                                                          .get(p.path)
                                                                      );
                                               return !p.path.last()
                                                             .isKey(name -> name.startsWith("b"));
                                           }
                                          );
        Assertions.assertFalse(obj1.stream_()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))));

        final JsObj obj2 = obj.get()
                              .filterKeys(p ->
                                          {
                                              Assertions.assertEquals(p.elem,
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
    void filter_keys_mutable()
    {


        final Supplier<JsObj> obj = () -> JsObj._of_("a",
                                                     JsObj._of_("a1",
                                                                JsStr.of("A"),
                                                                "b1",
                                                                JsStr.of("B")
                                                               ),
                                                     "b2",
                                                     JsStr.of("C"),
                                                     "d",
                                                     JsObj._of_("d1",
                                                                JsStr.of("D"),
                                                                "e1",
                                                                JsArray._of_(JsObj._of_("f",
                                                                                        JsStr.of("F1")
                                                                                       ),
                                                                             JsObj._of_("b3",
                                                                                        JsStr.of("G1"),
                                                                                        "c",
                                                                                        JsStr.of("G2")
                                                                                       )
                                                                            )
                                                               ),
                                                     "b4",
                                                     JsStr.of("F")
                                                    );


        final JsObj copy = obj.get();
        final JsObj obj1 = copy.filterKeys_(p ->
                                            {
                                                Assertions.assertEquals(p.elem,
                                                                        copy.get(p.path)
                                                                       );
                                                return !p.path.last()
                                                              .isKey(key -> key.startsWith("b"));
                                            });
        Assertions.assertFalse(obj1.stream_()
                                   .map(p -> p.path.last()
                                                   .asKey().name.startsWith("b"))
                                   .findFirst()
                                   .orElse(false)
                              );
        Assertions.assertEquals(obj1,
                                copy
                               );

        final JsObj copy1 = obj.get();
        final JsObj obj2 = copy1.filterKeys(p ->
                                            {
                                                Assertions.assertEquals(p.elem,
                                                                        copy1.get(p.path)
                                                                       );
                                                return !p.path.last()
                                                              .asKey().name.startsWith("b");
                                            }
                                           );
        Assertions.assertFalse(obj2.stream()
                                   .map(p -> p.path.last()
                                                   .asKey().name.startsWith("b"))
                                   .findFirst()
                                   .orElse(false)
                              );
        Assertions.assertEquals(obj2,
                                copy1
                               );


    }

    @Test
    void parse_string() throws MalformedJson
    {
        final JsObj mutable = JsObj._of_("a",
                                         JsObj._of_("a1",
                                                    JsStr.of("A"),
                                                    "b1",
                                                    JsStr.of("B")
                                                   ),
                                         "b2",
                                         JsStr.of("C"),
                                         "d",
                                         JsObj._of_("d1",
                                                    JsStr.of("D"),
                                                    "e1",
                                                    JsArray._of_(JsObj._of_("f",
                                                                            JsStr.of("F1")
                                                                           ),
                                                                 JsObj._of_("b3",
                                                                            JsStr.of("G1"),
                                                                            "c",
                                                                            JsStr.of("G2")
                                                                           )
                                                                )
                                                   ),
                                         "b4",
                                         JsStr.of("F")
                                        );

        final String str = mutable.toString();

        Assertions.assertEquals(mutable.toImmutable(),
                                JsObj.parse(str)
                                     .orElseThrow()
                               );
        Assertions.assertEquals(mutable,
                                JsObj._parse_(str)
                                     .orElseThrow()
                               );

        final TryObj tryObjImmutable = JsObj.parse(str,
                                                   ParseOptions.builder()
                                                               .withKeyFilter(p -> !p.last()
                                                                                     .isKey(it -> it.startsWith("b")))
                                                  );

        final TryObj tryObjMutable = JsObj._parse_(str,
                                                   ParseOptions.builder()
                                                               .withKeyFilter(p -> !p.last()
                                                                                     .isKey(it -> it.startsWith("b")))
                                                  );

        for (JsObj obj : Arrays.asList(tryObjImmutable.orElseThrow(),
                                       tryObjMutable.orElseThrow()
                                      ))
        {

            Assertions.assertFalse(obj
                                   .stream_()
                                   .map(p -> p.path.last()
                                                   .asKey().name.startsWith("b"))
                                   .findFirst()
                                   .orElse(false)
                                  );

            Assertions.assertEquals(4,
                                    obj
                                    .size_()
                                   );
        }


    }


    @Test
    void filter_jsons_from_immutable() throws MalformedJson
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
                                                  return !o.containsPath("R");
                                              });
        final JsObj result1 = obj.filterObjs_((p, o) ->
                                                {
                                                    Assertions.assertEquals(o,
                                                                            obj.get(p)
                                                                           );
                                                    return !o.containsPath("R");
                                                });


        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"R\":1},{\"T\":{\"R\":1},\"S\":1}],\"d\":{\"d\":3},\"e\": null}")
                                     .orElseThrow(),
                                result
                               );
        Assertions.assertEquals(JsObj.parse("{\"c\":[{\"S\":1}],\"d\":{\"d\":3},\"e\": null}")
                                     .orElseThrow(),
                                result1
                               );
    }


    @Test
    void filter_jsons_from_mutable() throws MalformedJson
    {
        final Supplier<JsObj> supplier = () -> JsObj._of_("a",
                                                          JsObj._of_("R",
                                                                     JsInt.of(1)
                                                                    ),
                                                          "b",
                                                          JsObj._of_("R",
                                                                     JsInt.of(1)
                                                                    ),
                                                          "c",
                                                          JsArray._of_(JsObj._of_("R",
                                                                                  JsInt.of(1)
                                                                                 ),
                                                                       JsObj._of_("S",
                                                                                  JsInt.of(1),
                                                                                  "T",
                                                                                  JsObj._of_("R",
                                                                                             JsInt.of(1)
                                                                                            )
                                                                                 )
                                                                      ),
                                                          "d",
                                                          JsObj._of_("d",
                                                                     JsInt.of(3)
                                                                    )
                                                         );
        final JsObj obj = JsObj._parse_(supplier.get()
                                                .toString())
                               .orElseThrow();


        final JsObj result = obj.filterObjs((p, o) ->
                                              {
                                                  Assertions.assertEquals(o,
                                                                          supplier.get()
                                                                                  .get(p)
                                                                         );
                                                  return !o.containsPath("R");
                                              });
        final JsObj obj1 = JsObj._parse_(supplier.get()
                                                 .toString())
                                .orElseThrow();

        final JsObj result_ = obj1.filterObjs_((p, o) ->
                                                 {
                                                     Assertions.assertEquals(o,
                                                                             supplier.get()
                                                                                     .get(p)
                                                                            );
                                                     return !o.containsPath("R");
                                                 });


        Assertions.assertEquals(JsObj._parse_("{\"c\":[{\"R\":1},{\"T\":{\"R\":1},\"S\":1}],\"d\":{\"d\":3}}")
                                     .orElseThrow(),
                                result
                               );
        Assertions.assertEquals(result,
                                obj
                               );
        Assertions.assertEquals(JsObj._parse_("{\"c\":[{\"S\":1}],\"d\":{\"d\":3}}")
                                     .orElseThrow(),
                                result_
                               );
        Assertions.assertEquals(result_,
                                obj1
                               );


    }

    @Test
    void filter_values_from_mutable_object() throws MalformedJson
    {

        Supplier<JsObj> supplier = () -> JsObj._of_("a",
                                                    JsInt.of(1),
                                                    "b",
                                                    NULL,
                                                    "c",
                                                    JsArray._of_(JsInt.of(1),
                                                                 NULL,
                                                                 JsInt.of(2),
                                                                 NULL,
                                                                 JsInt.of(3),
                                                                 JsArray._of_(JsInt.of(1),
                                                                              NULL,
                                                                              JsInt.of(2)
                                                                             ),
                                                                 JsObj._of_("a",
                                                                            NULL,
                                                                            "b",
                                                                            JsInt.of(1)
                                                                           )
                                                                )
                                                   );
        final JsObj obj = supplier.get();
        obj.filterElems_(p ->
                          {
                              Assertions.assertEquals(p.elem,
                                                      supplier.get()
                                                              .get(p.path)
                                                     );
                              return p.elem.isNotNull();
                          });

        Assertions.assertEquals(JsObj._parse_("{\"a\":1,\"c\":[1,2,3,[1,2],{\"b\":1}]}\n")
                                     .orElseThrow(),
                                obj
                               );

        final JsObj obj1 = supplier.get();
        obj1.filterElems(p ->
                          {
                              Assertions.assertEquals(p.elem,
                                                      supplier.get()
                                                              .get(p.path)
                                                     );
                              return p.elem.isNotNull();
                          });

        Assertions.assertEquals(JsObj._parse_("{\"a\":1,\"c\":[1,null,2,null,3,[1,null,2],{\"a\":null,\"b\":1}]}")
                                     .orElseThrow(),
                                obj1
                               );

    }

    @Test
    void filter_values_from_immutable_object() throws MalformedJson
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

        final JsObj obj1 = obj.filterElems_(p -> p.elem.isNotNull());

        Assertions.assertEquals(JsObj.parse("{\"a\":1,\"c\":[1,2,3,[1,2],{\"b\":1}]}\n")
                                     .orElseThrow(),
                                obj1
                               );

    }

    @Test
    void put_and_get()
    {
        final JsObj empty = JsObj.empty();
        final JsObj a = empty.put("a",
                                  JsBigDec.of(BigDecimal.valueOf(0.1d))
                                 );

        Assertions.assertEquals(OptionalDouble.of(0.1d),
                                a.getDouble("a")
                               );
        Assertions.assertEquals(Optional.of(BigDecimal.valueOf(0.1d)),
                                a.getBigDecimal("a")
                               );
    }

    @Test
    void map_json_obj_mutable()
    {
        Supplier<JsObj> supp = () -> JsObj._of_("a",
                                                JsObj._empty_(),
                                                "b",
                                                JsObj._empty_(),
                                                "c",
                                                JsObj._empty_(),
                                                "d",
                                                JsObj._of_("a",
                                                           JsObj._empty_(),
                                                           "b",
                                                           JsArray._of_(JsObj._empty_(),
                                                                        JsObj._empty_(),
                                                                        JsArray._of_(JsObj._empty_(),
                                                                                     NULL,
                                                                                     JsObj._empty_()
                                                                                    )
                                                                       )
                                                ,
                                                           "c",
                                                           JsObj._of_("a",
                                                                      TRUE
                                                                     )
                                                          )

                                               );

        final JsObj obj = supp.get();

        obj.mapObjs_((p, o) -> o.put("path",
                                     p.toString()
                                    ),
                     (p, o) -> o.isEmpty()
                    );
        supp.get()
            .stream_()
            .filter(p -> p.elem.isJson())
            .forEach(it ->
                     {
                         final JsPath path = it.path;
                         final JsObj o = ((JsObj) it.elem);
                         if (o.isEmpty()) Assertions.assertEquals(Optional.of(path.toString()),
                                                                  obj.getStr(path.append(of("path")))

                                                                 );


                     });
    }

    @Test
    void parse_with_options() throws MalformedJson
    {
        Supplier<JsObj> supplier = () -> JsObj._of_("a",
                                                    JsStr.of("1"),
                                                    "b",
                                                    JsInt.of(1),
                                                    "c",
                                                    JsObj._of_("a",
                                                               TRUE,
                                                               "b",
                                                               JsArray._of_(NULL,
                                                                            FALSE,
                                                                            JsInt.of(1),
                                                                            JsStr.of("A"),
                                                                            JsBigDec.of(BigDecimal.ONE),
                                                                            JsDouble.of(1.5d)
                                                                           )
                                                              ),
                                                    "d",
                                                    NULL,
                                                    "e",
                                                    TRUE,
                                                    "f",
                                                    FALSE
                                                   );
        Assertions.assertEquals(supplier.get()
                                        .filterElems_(p -> p.elem.isNotNull()),
                                JsObj._parse_(supplier.get()
                                                      .toString(),
                                              ParseOptions.builder()
                                                          .withElemFilter(p -> p.elem.isNotNull())
                                             )
                                     .orElseThrow()
                               );
    }

    @Test
    void test_operations()
    {

        JsObj obj = JsObj.of(JsPair.of("a.b.c",
                                       JsInt.of(1)
                                      ),
                             JsPair.of("a.b.d",
                                       JsInt.of(2)
                                      )
                            );

        Assertions.assertEquals(obj.appendAll(JsPath.of("0"),
                                              JsArray.of(1,
                                                         2
                                                        )
                                             ),
                                obj
                               );

        Assertions.assertEquals(obj.prependAll(JsPath.of("0"),
                                               JsArray.of(1,
                                                          2
                                                         )
                                              ),
                                obj
                               );
        Assertions.assertEquals(NOTHING,
                                obj.get("0")
                               );
        Assertions.assertEquals(obj,
                                obj.put("0",
                                        a -> NULL
                                       )
                               );

        Assertions.assertEquals(obj,
                                obj.remove("a.0")
                               );

        Assertions.assertEquals(obj,
                                obj.remove("0")
                               );

        Assertions.assertEquals(obj,
                                obj.remove("a.b.c.d")
                               );

        Assertions.assertEquals(obj,
                                obj.remove("a.b.c.0")
                               );
    }


    @Test
    void error_when_mixing_implementations()
    {

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsObj._of_("a",
                                                 JsObj.of("a",
                                                          NULL
                                                         )
                                                )
                               );
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray._of_(NULL,
                                                   TRUE,
                                                   FALSE,
                                                   JsObj.of("a",
                                                            NULL
                                                           )
                                                  )
                               );
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsObj.of("a",
                                               JsObj._of_("a",
                                                          NULL
                                                         )
                                              )
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray.of(NULL,
                                                 TRUE,
                                                 FALSE,
                                                 JsObj._of_("a",
                                                            NULL
                                                           )
                                                )
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray._of_(Arrays.asList(JsArray.of(1,
                                                                            2
                                                                           ))
                                                  )
                               );
    }


    @Test
    void test_parse_into_immutable() throws MalformedJson
    {
        JsObj obj = JsObj.of(JsPair.of("a.b.0",
                                       NULL
                                      ),
                             JsPair.of("a.b.1",
                                       TRUE
                                      ),
                             JsPair.of("a.b.c",
                                       FALSE
                                      ),
                             JsPair.of("a.b.c.d",
                                       JsInt.of(1)
                                      ),
                             JsPair.of("a.a.a",
                                       JsStr.of("a")
                                      ),
                             JsPair.of("a.b.0",
                                       JsBigDec.of(BigDecimal.ONE)
                                      )
                            );

        Assertions.assertEquals(obj,
                                JsObj.parse(obj.toString())
                                     .orElseThrow()
                               );

        Assertions.assertEquals(obj,
                                Json.parse(obj.toString())
                                    .objOrElseThrow()
                               );

    }

    @Test
    void test_parse_into_mutable() throws MalformedJson
    {
        JsObj obj = JsObj._of_(JsPair.of("a.b.0",
                                         NULL
                                        ),
                               JsPair.of("a.b.1",
                                         TRUE
                                        ),
                               JsPair.of("a.b.c",
                                         FALSE
                                        ),
                               JsPair.of("a.b.c.d",
                                         JsInt.of(1)
                                        ),
                               JsPair.of("a.a.a",
                                         JsStr.of("a")
                                        ),
                               JsPair.of("a.b.0",
                                         JsBigDec.of(BigDecimal.ONE)
                                        )
                              );

        Assertions.assertEquals(obj,
                                JsObj._parse_(obj.toString())
                                     .orElseThrow()
                               );

        Assertions.assertEquals(obj,
                                Json._parse_(obj.toString())
                                    .objOrElseThrow()
                               );

    }

    @Test
    void test_map_json_mutable_obj()
    {

        Supplier<JsObj> supplier = () -> JsObj._of_("a",
                                                    JsObj._empty_(),
                                                    "b",
                                                    JsStr.of("B"),
                                                    "c",
                                                    JsObj._of_("a",
                                                               JsStr.of("a"),
                                                               "b",
                                                               JsInt.of(1),
                                                               "e",
                                                               JsArray._of_(JsObj._of_("a",
                                                                                       JsStr.of("a"),
                                                                                       "b",
                                                                                       JsInt.of(1)
                                                                                      ),
                                                                            NULL,
                                                                            TRUE
                                                                           ),
                                                               "h",
                                                               JsArray._of_(JsObj._of_("c",
                                                                                       JsStr.of("C"),
                                                                                       "d",
                                                                                       JsStr.of("D")
                                                                                      ),
                                                                            JsObj._of_("d",
                                                                                       JsStr.of("D"),
                                                                                       "e",
                                                                                       JsStr.of("E"),
                                                                                       "f",
                                                                                       JsObj._of_("g",
                                                                                                  JsStr.of("G"),
                                                                                                  "h",
                                                                                                  JsStr.of("H")
                                                                                                 )
                                                                                      )
                                                                           )
                                                              )
                                                   );


        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put("size",
                                                                                    json.size()
                                                                                   );
        final JsObj obj = supplier.get();
        final JsObj newObj = obj.mapObjs_((p, o) ->
                                            {
                                                Assertions.assertEquals(o,
                                                                        supplier.get()
                                                                                .get(p)
                                                                       );
                                                return addSizeFn.apply(p,
                                                                       o
                                                                      );
                                            },
                                          (p, o) -> o.isNotEmpty()
                                         );

        Assertions.assertEquals(obj,
                                newObj
                               );

        final JsObj obj1 = supplier.get();
        final JsObj newObj1 = obj1.mapObjs((p, o) ->
                                             {
                                                 Assertions.assertEquals(o,
                                                                         supplier.get()
                                                                                 .get(p)
                                                                        );
                                                 return addSizeFn.apply(p,
                                                                        o
                                                                       );
                                             },
                                           (p, o) -> o.isNotEmpty()
                                          );
        Assertions.assertEquals(obj1,
                                newObj1
                               );


    }


    @Test
    void map_values_mutable() throws MalformedJson
    {

        Supplier<JsObj> supplier = () -> JsObj._of_("a",
                                                    JsInt.of(1),
                                                    "b",
                                                    NULL,
                                                    "c",
                                                    JsInt.of(3),
                                                    "d",
                                                    FALSE,
                                                    "e",
                                                    JsArray._of_(NULL,
                                                                 JsArray._of_(1,
                                                                              2,
                                                                              3
                                                                             ),
                                                                 JsInt.of(1),
                                                                 JsObj._of_("a",
                                                                            JsInt.of(1),
                                                                            "b",
                                                                            JsInt.of(2)
                                                                           ),
                                                                 NULL,
                                                                 JsArray._of_(JsInt.of(1),
                                                                              JsStr.of("a"),
                                                                              TRUE
                                                                             )
                                                                )
                                                   );

        final JsObj obj = supplier.get();
        obj.mapElems(pair ->
                      {
                          Assertions.assertEquals(pair.elem,
                                                  supplier.get()
                                                          .get(pair.path)
                                                 );
                          return Utils.mapIfInt(i -> i + 10)
                                      .andThen(p -> p.elem)
                                      .apply(pair);
                      },
                     p -> p.elem.isInt()
                    );


        Assertions.assertEquals(JsObj._parse_("{\"a\":11,\"b\":null,\"c\":13,\"d\":false,\"e\":[null,[1,2,3],1,{\"a\":1,\"b\":2},null,[1,\"a\",true]]}\n")
                                     .orElseThrow(),
                                obj
                               );

        final JsObj obj1 = supplier.get();
        obj1.mapElems_(pair ->
                        {
                            Assertions.assertEquals(pair.elem,
                                                    supplier.get()
                                                            .get(pair.path)
                                                   );
                            return Utils.mapIfInt(i -> i + 10)
                                        .andThen(p -> p.elem)
                                        .apply(pair);
                        },
                       p -> p.elem.isInt()
                      );

        Assertions.assertEquals(JsObj._parse_("{\"a\":11,\"b\":null,\"c\":13,\"d\":false,\"e\":[null,[11,12,13],11,{\"a\":11,\"b\":12},null,[11,\"a\",true]]}\n")
                                     .orElseThrow(),
                                obj1
                               );
    }

    @Test
    void equals()
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
    void test_map_json_immutable_obj() throws MalformedJson
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


        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put("size",
                                                                                    json.size()
                                                                                   );
        final JsObj newObj = obj.mapObjs_((p, o) ->
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

        Assertions.assertEquals(Json.parse("{\"a\":\"A\",\"b\":{},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}\n")
                                    .objOrElseThrow(),
                                newObj
                               );

        Assertions.assertEquals(JsObj.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                                     .orElseThrow(),
                                obj.mapObjs_((p, o) ->
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
                                     .orElseThrow(),
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


        Assertions.assertEquals(Json.parse("{\"a\":{\"size\":2,\"b\":\"B\",\"c\":\"C\"},\"b\":{},\"c\":[],\"d\":{\"e\":\"E\",\"size\":3,\"f\":\"F\",\"g\":{\"i\":\"I\",\"h\":\"H\"}}}\n")
                                    .objOrElseThrow(),
                                newObj1
                               );

    }

    @Test
    void test_throws_malformed_json_error()
    {
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": 1,[]}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": 1,\"b\"{}}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": 1,\"b\": {}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": 1,\"b\": [1,2]")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": 1,\"b\": [1,2,{2: 1}]}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": tv}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": tra}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": truf}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": trued}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": f1}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": fae}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": falf}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": falsi}")
                                           .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": falsea}")
                                           .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": \"\\u-0026\"}")
                                           .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": -12a}")
                                           .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": \"\\x\"}")
                                           .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": nall}")
                                           .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": nuxx}")
                                           .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": nulx}")
                                           .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsObj.parse("{\"a\": nullx}")
                                           .orElseThrow()
                               );


    }

    @Test
    void test_contains_element_in_object()
    {
        final JsObj obj = JsObj.of("a",
                                   JsInt.of(1),
                                   "b",
                                   JsArray.of(JsInt.of(2),
                                              JsObj.of("a",
                                                       NULL
                                                      )
                                             )
                                  );
        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, JsElem> next = iterator.next();
            Assertions.assertTrue(obj.containsElem(next.getValue()));
        }
        final JsObj _obj_ = JsObj._of_("a",
                                       JsInt.of(1),
                                       "b",
                                       JsArray._of_(JsInt.of(2),
                                                    JsObj._of_("a",
                                                               NULL
                                                              )
                                                   )
                                      );

        final Iterator<Map.Entry<String, JsElem>> iterator1 = _obj_.iterator();
        while (iterator1.hasNext())
        {
            Map.Entry<String, JsElem> next = iterator1.next();
            Assertions.assertTrue(_obj_.containsElem(next.getValue()));

        }
        Assertions.assertTrue(obj.containsElem(JsInt.of(1)));
        Assertions.assertTrue(_obj_.containsElem(JsInt.of(1)));
        Assertions.assertFalse(obj.containsElem(JsInt.of(2)));
        Assertions.assertFalse(_obj_.containsElem(JsInt.of(2)));
        Assertions.assertTrue(obj.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(_obj_.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(obj.containsElem_(NULL));
        Assertions.assertTrue(_obj_.containsElem_(NULL));
        Assertions.assertTrue(obj.containsPath("b.0"));
        Assertions.assertTrue(_obj_.containsPath("b.1.a"));
        Assertions.assertTrue(obj.containsPath("a"));
        Assertions.assertTrue(_obj_.containsPath("b"));
        Assertions.assertFalse(obj.containsPath("3"));
        Assertions.assertFalse(_obj_.containsPath("3"));
        Assertions.assertFalse(obj.containsPath("1.b"));
        Assertions.assertFalse(_obj_.containsPath("1.b"));

    }


}
