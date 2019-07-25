import jsonvalues.*;
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
import static jsonvalues.JsElems.mapIfInt;
import static jsonvalues.JsNull.NULL;

class TestJsArray
{


    @Test
    void creation_mutable_one_element_json_array()
    {
        JsArray arr = JsArray._of_(NULL);
        arr.prepend(JsStr.of("a"));

        Assertions.assertEquals(arr.head(),
                                JsStr.of("a")
                               );

        Assertions.assertEquals(arr.tail()
                                   .head(),
                                NULL
                               );
    }

    @Test
    void creation_mutable_two_element_json_array()
    {
        Supplier<JsArray> supplier = () -> JsArray._of_(JsInt.of(1),
                                                        TRUE,
                                                        JsInt.of(2),
                                                        JsStr.of("a"),
                                                        NULL,
                                                        JsArray._of_(JsObj._of_("a",
                                                                                JsInt.of(1),
                                                                                "b",
                                                                                NULL,
                                                                                "c",
                                                                                JsInt.of(3)
                                                                               ))
                                                       );


        final int result = supplier.get()
                                   .mapElems(Utils.mapIfInt(i -> i + 100)
                                                  .andThen(p -> p.elem)
                                            )
                                   .reduce(Integer::sum,
                                           pair -> pair.elem.asJsInt().x,
                                           pair -> pair.elem.isInt()
                                          )
                                   .orElse(-1);


        Assertions.assertEquals(result,
                                203
                               );

        final int result1 = supplier.get()
                                    .mapElems(Utils.mapIfInt(i -> i + 100)
                                                   .andThen(p -> p.elem),
                                              p -> p.elem.isInt(i -> i > 1)
                                             )
                                    .reduce(Integer::sum,
                                            pair -> pair.elem.asJsInt().x,
                                            p -> p.elem.isInt()
                                           )
                                    .orElse(-1);

        Assertions.assertEquals(result1,
                                103
                               );

        final int result3 = supplier.get()
                                    .mapElems_(p -> p.elem.asJsInt()
                                                          .map(i -> i + 100),
                                               p -> p.elem.isInt()
                                              )
                                    .reduce_(Integer::sum,
                                             pair -> pair.elem.asJsInt().x,
                                             pair -> pair.elem.isInt()
                                            )
                                    .orElse(-1);


        Assertions.assertEquals(result3,
                                407
                               );

    }

    @Test
    void creation_immutable_two_element_json_array()
    {
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


        final int result = arr.mapElems(Utils.mapIfInt(i -> i + 100)
                                             .andThen(p -> p.elem)
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


        Assertions.assertEquals(result,
                                203
                               );

        Assertions.assertEquals(result1,
                                203
                               );


        final int result2 = arr.mapElems_(Utils.mapIfInt(i -> i + 100)
                                               .andThen(p -> p.elem)
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
        Assertions.assertEquals(result2,
                                406
                               );
        Assertions.assertEquals(result3,
                                406
                               );
    }

    @Test
    void creation_mutable_three_element_json_array()
    {
        JsArray arr = JsArray._of_(JsStr.of("a"),
                                   JsStr.of("b"),
                                   JsStr.of("c")
                                  );
        Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            JsElem elem = iterator.next();
            if (elem.isStr(s -> s.equals("b"))) iterator.remove();
        }

        Assertions.assertEquals(arr,
                                JsArray._of_("a",
                                             "c"
                                            )
                               );

    }

    @Test
    void creation_mutable_four_element_json_array()
    {
        JsArray arr = JsArray._of_(JsLong.of(10),
                                   JsStr.of("b"),
                                   JsStr.of("c"),
                                   JsInt.of(10)
                                  );

        arr.filterElems(p -> p.elem.isIntegral());


        Assertions.assertTrue(arr.stream_()
                                 .allMatch(p -> p.elem.isIntegral())
                             );
    }

    @Test
    void creation_mutable_five_element_json_array()
    {
        JsArray arr = JsArray._of_(JsArray._of_(NULL,
                                                TRUE
                                               ),
                                   JsStr.of("A"),
                                   JsStr.of("B"),
                                   JsInt.of(1),
                                   JsStr.of("C"),
                                   JsStr.of("D"),
                                   JsStr.of("E"),
                                   JsObj._of_("a",
                                              NULL
                                             )
                                  );
        arr.mapElems(Utils.mapIfStr(String::toLowerCase)
                          .andThen(p -> p.elem)); // arr is mutable

        // ["a","b","c","d","e"]


        Assertions.assertEquals(arr,
                                JsArray._of_(JsArray._of_(NULL,
                                                          TRUE
                                                         ),
                                             JsStr.of("a"),
                                             JsStr.of("b"),
                                             JsInt.of(1),
                                             JsStr.of("c"),
                                             JsStr.of("d"),
                                             JsStr.of("e"),
                                             JsObj._of_("a",
                                                        NULL
                                                       )
                                            )
                               );
    }

    @Test
    void creation_mutable_json_array_containing_arbitrary_number_of_elements()
    {
        JsArray arr = JsArray._of_(JsStr.of("A"),
                                   JsStr.of("B"),
                                   JsStr.of("C"),
                                   JsStr.of("D"),
                                   JsStr.of("E"),
                                   JsStr.of("F"),
                                   JsStr.of("G")
                                  );

        arr.mapElems(Utils.mapIfStr(String::toLowerCase)
                          .andThen(p -> p.elem))
           .filterElems(p -> p.elem.isStr(letter -> Comparator.<String>naturalOrder()
           .compare(letter,
                    "d"
                   ) < 0));
        Assertions.assertEquals(arr,
                                JsArray._of_("a",
                                             "b",
                                             "c"
                                            )
                               );

    }


    @Test
    void parse_string_into_mutable_json_array() throws MalformedJson
    {

        Assertions.assertEquals(JsArray._parse_("[1,2]")
                                       .orElseThrow(),
                                JsArray._of_(1,
                                             2
                                            )
                               );

        final Optional<JsArray> opt = JsArray._parse_("[1,2]")
                                             .toOptional();

        Assertions.assertTrue(opt.isPresent());


        final Optional<JsArray> optEmpty = JsArray._parse_("[1,2")
                                                  .toOptional();

        Assertions.assertFalse(optEmpty.isPresent());

        Assertions.assertThrows(MalformedJson.class,
                                () -> JsArray._parse_("[1,2")
                                             .orElseThrow()
                               );


        Assertions.assertEquals(JsArray._parse_("[1,2")
                                       .orElse(JsArray::_empty_),
                                JsArray._empty_()
                               );


    }

    @Test
    void parse_string_into_mutable_json_array_mapping_and_filtering_elements_while_the_parsing() throws MalformedJson
    {

        String str = "[1,2,3,true,false]";
        final JsArray array = JsArray._parse_(str,
                                              ParseOptions.builder()
                                                          .withElemFilter(p -> p.elem.isInt())
                                                          .withElemMap(Utils.mapIfInt(i -> i + 1)
                                                                            .andThen(p -> p.elem))
                                             )
                                     .orElseThrow();

        Assertions.assertEquals(array,
                                JsArray._of_(2,
                                             3,
                                             4
                                            )
                               );
    }


    @Test
    void creation_of_mutable_empty_json_array()
    {

        JsArray arr = JsArray._empty_();
        Assertions.assertEquals(0,
                                arr.size()
                               );

        arr.append(JsInt.of(1));  // arr is mutable
        Assertions.assertEquals(1,
                                arr.size()
                               );

    }

    @Test
    void modification_of_mutable_json_array_throws_exception_because_it_was_created_from_immutable_list()
    {

        //List.of creates and immutable list, so every modification throws an exception

        final JsArray arr = JsArray._of_(Arrays.asList(JsStr.of("a"),
                                                       JsStr.of("b")
                                                      )
                                        );
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> arr.remove("-1")
                               );


    }

    @Test
    void modifications_of_mutable_json_array_created_from_list_are_reflected_in_the_list()
    {

        //List.of creates and immutable list, so every modification throws an exception
        final ArrayList<JsElem> list = new ArrayList<>();
        list.add(JsStr.of("a"));
        list.add(JsStr.of("b"));

        JsArray arr = JsArray._of_(list);
        arr.remove("-1");

        Assertions.assertEquals(arr.size(),
                                list.size()
                               );


    }


    @Test
    void appending_a_json_array_to_the_back_of_another_json_array()
    {
        JsArray arr = JsArray.of("a",
                                 "b"
                                );
        JsArray arr1 = arr.appendAll(JsArray.of(NULL,
                                                TRUE,
                                                FALSE
                                               ));

        Assertions.assertTrue(arr.size() == 2);
        Assertions.assertTrue(arr1.size() == 5);
        Assertions.assertEquals(arr,
                                JsArray.of("a",
                                           "b"
                                          )
                               );
        Assertions.assertEquals(arr1,
                                JsArray.of(JsStr.of("a"),
                                           JsStr.of("b"),
                                           NULL,
                                           TRUE,
                                           FALSE
                                          )
                               );

        JsArray _arr_ = JsArray._of_("a",
                                     "b"
                                    );

        _arr_.appendAll(JsArray.of(NULL,
                                   TRUE,
                                   FALSE
                                  ));

        Assertions.assertTrue(_arr_.size() == 5);

        Assertions.assertEquals(_arr_,
                                JsArray._of_(JsStr.of("a"),
                                             JsStr.of("b"),
                                             NULL,
                                             TRUE,
                                             FALSE
                                            )
                               );
    }


    @Test
    void prepending_a_json_array_to_the_front_of_another_json_array()
    {
        JsArray arr = JsArray.of("a",
                                 "b"
                                );
        JsArray arr1 = arr.prependAll(JsArray.of(NULL,
                                                 TRUE,
                                                 FALSE
                                                ));

        Assertions.assertEquals(2,
                                arr.size()
                               );
        Assertions.assertTrue(arr1.size() == 5);
        Assertions.assertEquals(arr,
                                JsArray.of("a",
                                           "b"
                                          )
                               );
        Assertions.assertEquals(arr1,
                                JsArray.of(NULL,
                                           TRUE,
                                           FALSE,
                                           JsStr.of("a"),
                                           JsStr.of("b")
                                          )
                               );

        JsArray _arr_ = JsArray._of_("a",
                                     "b"
                                    );

        _arr_.prependAll(JsArray.of(NULL,
                                    TRUE,
                                    FALSE
                                   ));

        Assertions.assertTrue(_arr_.size() == 5);

        Assertions.assertEquals(_arr_,
                                JsArray._of_(NULL,
                                             TRUE,
                                             FALSE,
                                             JsStr.of("a"),
                                             JsStr.of("b")
                                            )
                               );


    }


    @Test
    void appends_one_or_more_elements_to_the_back_of_a_json_array()
    {

        JsArray arr = JsArray.of("a",
                                 "b"
                                );
        JsArray arr1 = arr.append(JsStr.of("c"),
                                  JsStr.of("d")
                                 ); // ["a","b","c","d"]

        Assertions.assertEquals(JsArray.of("a",
                                           "b",
                                           "c",
                                           "d"
                                          ),
                                arr1
                               );

        JsArray _arr_ = JsArray._of_("a",
                                     "b"
                                    );
        _arr_.append(JsStr.of("c"),
                     JsStr.of("d")
                    );

        Assertions.assertEquals(JsArray._of_("a",
                                             "b",
                                             "c",
                                             "d"
                                            ),
                                _arr_
                               );
    }


    @Test
    void prepend()
    {

        JsArray arr = JsArray.of("a",
                                 "b"
                                );

        Assertions.assertEquals(JsArray.of("c",
                                           "d",
                                           "a",
                                           "b"
                                          ),
                                arr.prepend(JsStr.of("c"),
                                            JsStr.of("d")
                                           )
                               );

        Assertions.assertTrue(arr.size() == 2);


        JsArray _arr_ = JsArray._of_("a",
                                     "b"
                                    );

        _arr_.prepend(JsStr.of("c"),
                      JsStr.of("d")
                     );

        Assertions.assertTrue(arr.size() == 2);
        Assertions.assertEquals(JsArray._of_("c",
                                             "d",
                                             "a",
                                             "b"
                                            ),
                                _arr_

                               );


    }


    @Test
    void collecting_an_array_from_a_collector() throws MalformedJson
    {

        final JsArray arr = JsArray.of(JsStr.of("a"),
                                       JsStr.of("b"),
                                       JsArray.of(JsObj.empty(),
                                                  TRUE,
                                                  FALSE,
                                                  NULL,
                                                  JsArray.empty()
                                                 ),
                                       JsStr.of("a")
                                      );
        Assertions.assertEquals(arr,
                                arr.stream_()
                                   .parallel()
                                   .collect(JsArray.collector())
                               );


        Assertions.assertEquals(JsArray._parse_(arr.toString())
                                       .orElseThrow(),
                                arr.stream_()
                                   .parallel()
                                   .collect(JsArray._collector_())
                               );

        Assertions.assertEquals(arr,
                                arr.toMutable()
                                   .toImmutable()
                               );
        final JsArray expected = arr.toMutable();
        Assertions.assertEquals(expected,
                                arr.stream_()
                                   .parallel()
                                   .collect(JsArray._collector_())
                               );
        Assertions.assertEquals(arr,
                                arr.toMutable()
                               );

    }


    @Test
    void contains_element_in_js_array()
    {
        JsArray arr = JsArray.of(JsInt.of(1),
                                 TRUE,
                                 JsStr.of("a"),
                                 NULL
                                );
        Assertions.assertTrue(arr.containsElem(JsInt.of(1)));   //true
        Assertions.assertTrue(arr.containsElem(TRUE));   //true
        Assertions.assertTrue(arr.containsElem(JsStr.of("a"))); //true
        Assertions.assertTrue(arr.containsElem(NULL));
        Assertions.assertFalse(arr.containsElem(JsInt.of(10)));


        JsArray _arr_ = JsArray.of(JsInt.of(1),
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
    void empty_immutable_js_array_returns_the_same_instance()
    {

        Assertions.assertSame(JsArray.empty(),
                              JsArray.empty()
                             );
    }

    @Test
    void test_head_of_json_array_returns_the_firt_element_or_an_exception()
    {

        JsArray arr = JsArray.of(JsInt.of(1),
                                 TRUE
                                );
        Assertions.assertEquals(JsInt.of(1),
                                arr.head()

                               );

        Assertions.assertSame(TRUE,
                              arr.tail()
                                 .head()

                             );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray.empty()
                                             .head()
                               );

        JsArray _arr_ = JsArray.of(JsInt.of(1),
                                   TRUE
                                  );
        Assertions.assertEquals(JsInt.of(1),
                                _arr_.head()

                               );

        Assertions.assertSame(TRUE,
                              _arr_.tail()
                                   .head()

                             );


        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray._empty_()
                                             .head()
                               );

    }

    @Test
    void test_init_of_json_array_returns_all_the_elements_except_the_last_or_an_exception()
    {

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

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray.empty()
                                             .init()
                               );

        JsArray _arr_ = JsArray._of_(JsInt.of(1),
                                     TRUE,
                                     JsStr.of("a"),
                                     NULL
                                    );

        Assertions.assertEquals(JsArray._of_(JsInt.of(1),
                                             TRUE,
                                             JsStr.of("a")
                                            ),
                                _arr_.init()
                               );

        Assertions.assertEquals(JsArray._of_(TRUE,
                                             JsStr.of("a")
                                            ),
                                _arr_.tail()
                                     .init()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray._empty_()
                                             .init()
                               );


    }


    @Test
    void last_returns_the_last_element_or_throws_exception_if_emtpy()
    {

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

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray.empty()
                                             .last()
                               );

        JsArray _arr_ = JsArray._of_(JsInt.of(1),
                                     TRUE,
                                     JsStr.of("a"),
                                     NULL
                                    );

        Assertions.assertEquals(NULL,
                                _arr_.last()
                               );

        Assertions.assertEquals(JsStr.of("a"),
                                _arr_.init()
                                     .last()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray._empty_()
                                             .last()
                               );

    }


    @Test
    void test_create_immutable_json_array_from_list_of_elements()
    {

        JsArray arr = JsArray.of(Arrays.asList(JsStr.of("a"),
                                               JsInt.of(1)
                                              )
                                );
        JsArray newArr = arr.remove("-1");

        Assertions.assertEquals(2,
                                arr.size()
                               );

        Assertions.assertEquals(1,
                                newArr.size()
                               );

    }

    @Test
    void test_create_one_element_immutable_json_array()
    {
        JsArray arr = JsArray.of(NULL);
        JsArray arr1 = arr.prepend(JsStr.of("a"));
        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        Assertions.assertEquals(JsStr.of("a"),
                                arr1.head()
                               );

    }

    @Test
    void test_create_two_elements_immutable_json_array()
    {

        JsArray arr = JsArray.of(JsInt.of(1),
                                 NULL,
                                 JsInt.of(2),
                                 JsObj.empty()
                                );
        JsArray newArr = arr.mapElems(Utils.mapIfInt(i -> i + 10)
                                           .andThen(p -> p.elem),
                                      p -> p.elem.isInt()
                                     );

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
    void test_create_three_elements_immutable_json_array()
    {

        JsArray arr = JsArray.of(JsStr.of("a"),
                                 JsStr.of("b"),
                                 JsStr.of("c")
                                );

        final JsArray newArr = arr.mapElems(Utils.mapIfStr(String::toUpperCase)
                                                 .andThen(p -> p.elem));

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
    void test_create_four_elements_immutable_json_array()
    {

        JsArray arr = JsArray.of(JsLong.of(10),
                                 JsStr.of("b"),
                                 JsStr.of("c"),
                                 JsInt.of(10)
                                );
        JsArray arr1 = arr.filterElems(p -> p.elem.isIntegral());

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        Assertions.assertEquals(JsArray.of(JsLong.of(10),
                                           JsInt.of(10)
                                          ),
                                arr1
                               );
    }

    @Test
    void test_create_five_elements_immutable_json_array()
    {

        JsArray arr = JsArray.of(JsStr.of("A"),
                                 JsStr.of("B"),
                                 JsStr.of("C"),
                                 JsStr.of("D"),
                                 JsStr.of("E")
                                );
        JsArray arr1 = arr.put("-1",
                               "F"
                              );

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );

        Assertions.assertEquals(JsStr.of("F"),
                                arr1.get("-1")
                               );

    }

    @Test
    void test_create_six_elements_imutable_json_array()
    {
        JsArray arr = JsArray.of(JsStr.of("A"),
                                 JsStr.of("B"),
                                 JsStr.of("C"),
                                 JsStr.of("D"),
                                 JsStr.of("E"),
                                 JsStr.of("F"),
                                 JsStr.of("G")
                                );

        JsArray arr1 = arr.mapElems(pair -> Utils.mapIfStr(s ->
                                                           {
                                                               final int index = pair.path.last()
                                                                                          .asIndex().n;
                                                               return s.concat(String.valueOf(index));
                                                           })
                                                 .andThen(p -> p.elem)
                                                 .apply(pair)
                                   );

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
    void test_parse_string_into_immutable_json_array() throws MalformedJson
    {

        Assertions.assertEquals(JsArray.of(1,
                                           2
                                          ),
                                JsArray.parse("[1,2]")
                                       .orElseThrow()
                               );
        Assertions.assertEquals(Optional.of(JsArray.of(1,
                                                       2
                                                      )),
                                JsArray.parse("[1,2]")
                                       .toOptional()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> JsArray.parse("[1,2")
                                             .orElseThrow()
                               );

        Assertions.assertEquals(JsArray._empty_(),
                                JsArray.parse("[1,2")
                                       .orElse(JsArray::_empty_)
                               );


        Assertions.assertEquals(Optional.empty(),
                                JsArray.parse("[1,2")
                                       .toOptional()
                               ); // Optional.empty


    }

    @Test
    void tail_of_json_array_returns_all_elements_except_first_one()
    {

        JsArray arr = JsArray.of("a",
                                 "b",
                                 "c"
                                );
        Assertions.assertEquals(JsArray.of("b",
                                           "c"
                                          ),
                                arr.tail()
                               ); //  ["b","c"]

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray.empty()
                                             .tail()
                               );


        JsArray _arr_ = JsArray._of_("a",
                                     "b",
                                     "c"
                                    );
        Assertions.assertEquals(JsArray._of_("b",
                                             "c"
                                            ),
                                _arr_.tail()
                               ); //  ["b","c"]

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsArray._empty_()
                                             .tail()
                               );

    }

    @Test
    void parse_string_into_immutable_json_array_with_options() throws MalformedJson
    {

        final JsArray arr = JsArray.parse("[1,2,3,true,false,null,[null,true,4]]",
                                          ParseOptions.builder()
                                                      .withElemFilter(p -> p.elem.isInt())
                                                      .withElemMap(p -> mapIfInt(i -> i + 10).apply(p.elem))
                                         )
                                   .orElseThrow();

        Assertions.assertEquals(JsArray.of(JsInt.of(11),
                                           JsInt.of(12),
                                           JsInt.of(13),
                                           JsArray.of(14)
                                          ),
                                arr
                               );

    }


    @Test
    void static_factory_methods_from_primitives()
    {

        JsArray arr = JsArray.of(1,
                                 2,
                                 3
                                );
        Assertions.assertNotEquals(arr,
                                   arr.append(JsInt.of(4))
                                  );

        JsArray _arr_ = JsArray._of_(1,
                                     2,
                                     3
                                    );
        Assertions.assertEquals(_arr_,
                                _arr_.append(JsInt.of(4))
                               );

        JsArray arr1 = JsArray.of("a",
                                  "b",
                                  "c"
                                 );
        Assertions.assertNotEquals(arr1,
                                   arr1.append(JsStr.of("d"))
                                  );

        JsArray _arr1_ = JsArray._of_("a",
                                      "b",
                                      "c"
                                     );
        Assertions.assertEquals(_arr1_,
                                _arr1_.append(JsStr.of("d"))
                               );

        JsArray arr2 = JsArray.of(true,
                                  false
                                 );
        Assertions.assertNotEquals(arr2,
                                   arr2.append(JsBool.of(false))
                                  );

        JsArray _arr2_ = JsArray._of_(true,
                                      false
                                     );
        Assertions.assertEquals(_arr2_,
                                _arr2_.append(JsBool.of(false))
                               );

        JsArray arr3 = JsArray.of(1l,
                                  2l,
                                  3l
                                 );
        Assertions.assertNotEquals(arr3,
                                   arr3.append(JsLong.of(4))
                                  );

        JsArray _arr3_ = JsArray._of_(1l,
                                      2l,
                                      3l
                                     );
        Assertions.assertEquals(_arr3_,
                                _arr3_.append(JsLong.of(4))
                               );

        JsArray arr4 = JsArray.of(1.1d,
                                  2.2d,
                                  3.3d
                                 );
        Assertions.assertNotEquals(arr4,
                                   arr4.append(JsDouble.of(4.4d))
                                  );

        JsArray _arr4_ = JsArray._of_(1.1d,
                                      2.2d,
                                      3.3d
                                     );
        Assertions.assertEquals(_arr4_,
                                _arr4_.append(JsDouble.of(4.4d))
                               );

    }

    @Test
    void traversing_mutable_json_array_by_an_iterator_and_removing_some_elements()
    {

        final JsArray arr = JsArray._of_(1,
                                         2,
                                         3,
                                         4
                                        );
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            final JsElem next = iterator.next();
            if (next.isInt(i -> i == 3)) iterator.remove();
        }
        Assertions.assertEquals(arr,
                                JsArray._of_(1,
                                             2,
                                             4
                                            )
                               );
    }

    @Test
    void create_immutable_json_array_from_one_or_more_pairs() throws MalformedJson
    {

        final JsArray arr = JsArray.of(JsPair.of("0",
                                                 JsInt.of(1)
                                                ),
                                       JsPair.of("2",
                                                 JsInt.of(3)
                                                )
                                      );
        Assertions.assertEquals(JsArray.of(JsInt.of(1),
                                           NULL,
                                           JsInt.of(3)
                                          ),
                                arr
                               );

        final JsArray arr1 = JsArray.of(JsPair.of("0.a",
                                                  JsInt.of(1)
                                                 ),
                                        JsPair.of("2.b",
                                                  JsInt.of(3)
                                                 )
                                       );

        Assertions.assertEquals(JsArray.parse("[{\"a\": 1},null,{\"b\": 3}]")
                                       .orElseThrow(),
                                arr1
                               );


    }

    @Test
    void create_mutable_json_array_from_one_or_more_pairs() throws MalformedJson
    {

        final JsArray arr = JsArray._of_(JsPair.of("0",
                                                   JsInt.of(1)
                                                  ),
                                         JsPair.of("2",
                                                   JsInt.of(3)
                                                  )
                                        );
        Assertions.assertEquals(JsArray._of_(JsInt.of(1),
                                             NULL,
                                             JsInt.of(3)
                                            ),
                                arr
                               );

        final JsArray arr1 = JsArray._of_(JsPair.of("0.a",
                                                    JsInt.of(1)
                                                   ),
                                          JsPair.of("2.b",
                                                    JsInt.of(3)
                                                   )
                                         );

        Assertions.assertEquals(JsArray._parse_("[{\"a\": 1},null,{\"b\": 3}]")
                                       .orElseThrow(),
                                arr1
                               );


    }

    @Test
    void intersection() throws MalformedJson
    {

        final JsArray arr1 = JsArray.parse("[{\"a\": 1, \"b\": [1,2,2]}]")
                                    .orElseThrow();
        final JsArray arr2 = JsArray.parse("[{\"a\": 1, \"b\": [1,2]}]")
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
        Assertions.assertEquals(JsArray.empty(),
                                arr1.intersection(arr2,
                                                  LIST
                                                 )
                               );

        Assertions.assertEquals(JsArray.empty(),
                                arr1.intersection(arr2,
                                                  LIST
                                                 )
                               );

        Assertions.assertEquals(JsArray.empty(),
                                arr1.intersection(arr2,
                                                  SET
                                                 )
                               );

        Assertions.assertEquals(JsArray.empty(),
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
    void equals_and_hashcode()
    {
        final JsArray arr = JsArray.of(JsInt.of(1),
                                       JsBigInt.of(BigInteger.ONE),
                                       JsLong.of(1),
                                       JsBigDec.of(BigDecimal.ONE),
                                       JsDouble.of(1d)
                                      );
        final JsArray arr1 = JsArray.of(JsBigDec.of(BigDecimal.ONE),
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
    void reduce_strings()
    {
        final JsArray array = JsArray.of(JsStr.of("a"),
                                         JsStr.of("b"),
                                         JsInt.of(1),
                                         JsInt.of(2),
                                         JsObj.of("key",
                                                  JsStr.of("c"),
                                                  "key1",
                                                  JsStr.of("d"),
                                                  "key3",
                                                  JsArray.of("e",
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
    void filter_mutable_jsons() throws MalformedJson
    {
        Supplier<JsArray> supplier = () -> JsArray._of_(JsObj._of_("a",
                                                                   NULL
                                                                  ),
                                                        JsInt.of(1),
                                                        JsObj._of_("a",
                                                                   NULL
                                                                  ),
                                                        JsObj._of_("a",
                                                                   JsInt.of(1)
                                                                  ),
                                                        JsArray._of_(JsObj._of_("a",
                                                                                NULL
                                                                               ),
                                                                     JsObj._of_("a",
                                                                                JsInt.of(1)
                                                                               ),
                                                                     JsObj._of_("b",
                                                                                NULL
                                                                               ),
                                                                     JsObj._of_("a",
                                                                                NULL
                                                                               )
                                                                    )
                                                       );

        final JsArray arr = JsArray._parse_(supplier.get()
                                                    .toString())
                                   .orElseThrow();
        arr.filterObjs_((p, o) ->
                        {
                            Assertions.assertEquals(o,
                                                    supplier.get()
                                                            .get(p)
                                                   );
                            return o.get("a")
                                    .isNotNull();
                        });
        Assertions.assertEquals(JsArray._parse_("[1,{\"a\":1},[{\"a\":1},{\"b\":null}]]\n")
                                       .orElseThrow(),
                                arr
                               );

        final JsArray arr1 = JsArray._parse_(supplier.get()
                                                     .toString())
                                    .orElseThrow();

        arr1.filterObjs((p, o) ->
                        {
                            Assertions.assertEquals(o,
                                                    supplier.get()
                                                            .get(p)
                                                   );
                            return o.get("a")
                                    .isNotNull();
                        });

        Assertions.assertEquals(JsArray._parse_("[1,{\"a\":1},[{\"a\":null},{\"a\":1},{\"b\":null},{\"a\":null}]]\n")
                                       .orElseThrow(),
                                arr1
                               );
    }

    @Test
    void filter_immutable_jsons() throws MalformedJson
    {
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


        final JsArray arr1 = arr.filterObjs_((p, o) ->
                                             {
                                                 Assertions.assertEquals(o,
                                                                         arr.get(p)
                                                                        );
                                                 return o.get("a")
                                                         .isNotNull();
                                             });
        Assertions.assertEquals(JsArray.parse("[1,{\"a\":1},[{\"a\":1},{\"b\":null}]]\n")
                                       .orElseThrow(),
                                arr1
                               );


        final JsArray arr2 = arr.filterObjs((p, o) ->
                                            {
                                                Assertions.assertEquals(o,
                                                                        arr.get(p)
                                                                       );
                                                return o.get("a")
                                                        .isNotNull();
                                            });

        Assertions.assertEquals(JsArray.parse("[1,{\"a\":1},[{\"a\":null},{\"a\":1},{\"b\":null},{\"a\":null}]]\n")
                                       .orElseThrow(),
                                arr2
                               );
    }

    @Test
    void filter_keys_mutable() throws MalformedJson
    {
        JsArray array = JsArray._of_(JsObj._of_("a",
                                                NULL,
                                                "b",
                                                JsArray._of_(JsObj._of_("a",
                                                                        NULL,
                                                                        "b",
                                                                        JsInt.of(1)
                                                                       ),
                                                             JsObj._of_("a",
                                                                        NULL,
                                                                        "b",
                                                                        JsInt.of(1)
                                                                       ),
                                                             JsObj._of_("a",
                                                                        NULL,
                                                                        "b",
                                                                        JsInt.of(1)
                                                                       )
                                                            )
                                               ),
                                     JsObj._of_("a",
                                                NULL,
                                                "b",
                                                JsArray._of_(JsObj._of_("a",
                                                                        NULL,
                                                                        "b",
                                                                        JsInt.of(1)
                                                                       ),
                                                             JsObj._of_("a",
                                                                        NULL,
                                                                        "b",
                                                                        JsInt.of(1)
                                                                       ),
                                                             JsObj._of_("a",
                                                                        NULL,
                                                                        "b",
                                                                        JsInt.of(1)
                                                                       )
                                                            )
                                               )
                                    );

        array.filterKeys_(p -> p.elem.isNotNull());

        Assertions.assertEquals(JsArray._parse_("[{\"b\":[{\"b\":1},{\"b\":1},{\"b\":1}]},{\"b\":[{\"b\":1},{\"b\":1},{\"b\":1}]}]\n")
                                       .orElseThrow(),
                                array
                               );
    }

    @Test
    void parse_with_options_mutable() throws MalformedJson
    {
        final Supplier<JsArray> supplier = () -> JsArray._of_(NULL,
                                                              JsArray._of_(1,
                                                                           2
                                                                          ),
                                                              NULL,
                                                              JsArray._of_("a",
                                                                           "b"
                                                                          ),
                                                              NULL
                                                             );
        Assertions.assertEquals(supplier.get()
                                        .filterElems_(p ->
                                                      {
                                                          Assertions.assertEquals(p.elem,
                                                                                  supplier.get()
                                                                                          .get(p.path)
                                                                                 );
                                                          return p.elem.isNotNull();
                                                      }),
                                JsArray._parse_(supplier.get()
                                                        .toString(),
                                                ParseOptions.builder()
                                                            .withElemFilter(p -> p.elem.isNotNull())
                                               )
                                       .orElseThrow()
                               );
    }

    @Test
    void parse_with_options_immutable() throws MalformedJson
    {
        final JsArray array = JsArray.of(NULL,
                                         JsArray.of(1,
                                                    2
                                                   ),
                                         NULL,
                                         JsArray.of("a",
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
                                JsArray.parse(array.toString(),
                                              ParseOptions.builder()
                                                          .withElemFilter(p -> p.elem.isNotNull())
                                             )
                                       .orElseThrow()
                               );
    }


    @Test
    void map_json_immutable() throws MalformedJson
    {

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

        final JsArray a_ = arr.mapObjs_((path, obj) ->
                                        {
                                            Assertions.assertEquals(obj,
                                                                    arr.get(path)
                                                                   );
                                            return obj.put("size",
                                                           obj.size()
                                                          );
                                        });


        Assertions.assertEquals(JsArray.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"size\":2,\"d\":1},\"b\":2,\"c\":3}]\n")
                                       .orElseThrow(),
                                a_
                               );

        final JsArray a = arr.mapObjs((path, obj) ->
                                      {
                                          Assertions.assertEquals(obj,
                                                                  arr.get(path)
                                                                 );
                                          return obj.put("size",
                                                         obj.size()
                                                        );
                                      });

        Assertions.assertEquals(JsArray.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"d\":1},\"b\":2,\"c\":3}]\n")
                                       .orElseThrow(),
                                a
                               );
    }

    @Test
    void map_json_immutable_with_predicate() throws MalformedJson
    {

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

        final JsArray a = arr.mapObjs_((path, obj) ->
                                       {
                                           Assertions.assertEquals(obj,
                                                                   arr.get(path)
                                                                  );
                                           return obj.put("size",
                                                          obj.size()
                                                         );
                                       },
                                       (p, obj) -> obj.isNotEmpty()
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
                                       .orElseThrow(),
                                a
                               );
    }

    @Test
    void map_json_mutable() throws MalformedJson
    {

        Supplier<JsArray> supplier = () -> JsArray._of_(JsObj._of_("a",
                                                                   JsInt.of(1),
                                                                   "b",
                                                                   JsInt.of(2)
                                                                  ),
                                                        JsStr.of("c"),
                                                        TRUE,
                                                        FALSE,
                                                        JsArray._of_(JsObj._of_("a",
                                                                                JsInt.of(1)
                                                                               ),
                                                                     NULL,
                                                                     JsObj._of_("a",
                                                                                JsInt.of(1),
                                                                                "b",
                                                                                JsInt.of(2)
                                                                               ),
                                                                     TRUE,
                                                                     FALSE
                                                                    ),
                                                        JsObj._of_("a",
                                                                   JsInt.of(1),
                                                                   "b",
                                                                   JsInt.of(2),
                                                                   "c",
                                                                   JsInt.of(3)
                                                                  )
                                                       );

        JsArray arr = supplier.get();
        final JsArray a_ = arr.mapObjs_((path, obj) ->
                                        {
                                            Assertions.assertEquals(obj,
                                                                    supplier.get()
                                                                            .get(path)
                                                                   );
                                            return obj.put("size",
                                                           obj.size()
                                                          );
                                        });

        Assertions.assertEquals(arr,
                                a_
                               );
        Assertions.assertEquals(JsArray._parse_("[{\"a\":1,\"b\":2,\"size\":2},\"c\",true,false,[{\"a\":1,\"size\":1},null,{\"a\":1,\"b\":2,\"size\":2},true,false],{\"a\":1,\"b\":2,\"c\":3,\"size\":3}]\n")
                                       .orElseThrow(),
                                a_
                               );
        final JsArray a = supplier.get()
                                  .mapObjs((path, obj) ->
                                           {
                                               Assertions.assertEquals(obj,
                                                                       supplier.get()
                                                                               .get(path)
                                                                      );
                                               return obj.put("size",
                                                              obj.size()
                                                             );
                                           });
        Assertions.assertEquals(JsArray._parse_("[{\"a\":1,\"b\":2,\"size\":2},\"c\",true,false,[{\"a\":1},null,{\"a\":1,\"b\":2},true,false],{\"a\":1,\"b\":2,\"c\":3,\"size\":3}]\n")
                                       .orElseThrow(),
                                a
                               );
    }

    @Test
    void test_map_json_mutable_array()
    {

        final Supplier<JsArray> supplier = () -> JsArray._of_(JsObj._of_("a",
                                                                         JsStr.of("A"),
                                                                         "b",
                                                                         JsStr.of("B")
                                                                        ),
                                                              JsObj._empty_(),
                                                              JsArray._empty_(),
                                                              NULL,
                                                              JsObj._of_("c",
                                                                         JsStr.of("C"),
                                                                         "d",
                                                                         JsStr.of("D"),
                                                                         "e",
                                                                         JsObj._of_("f",
                                                                                    JsStr.of("F")
                                                                                   )
                                                                        )
                                                             );

        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put("size",
                                                                                    json.size()
                                                                                   );

        JsArray arr = supplier.get();
        final JsArray newArr = arr.mapObjs((p, o) ->
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


        Assertions.assertEquals(arr,
                                newArr
                               );

        final Supplier<JsArray> supplier1 = () -> JsArray._of_(JsObj._of_("a",
                                                                          JsStr.of("A"),
                                                                          "b",
                                                                          JsStr.of("B")
                                                                         ),
                                                               NULL,
                                                               JsObj._empty_(),
                                                               JsArray._empty_(),
                                                               JsObj._of_("c",
                                                                          JsStr.of("C"),
                                                                          "d",
                                                                          JsStr.of("D"),
                                                                          "e",
                                                                          JsObj._of_("f",
                                                                                     JsStr.of("F"),
                                                                                     "g",
                                                                                     JsObj._of_("h",
                                                                                                JsStr.of("H")
                                                                                               )
                                                                                    )
                                                                         )
                                                              );

        final JsArray arr1 = supplier1.get();
        final JsArray newArr1 = arr1.mapObjs_((p, o) ->
                                              {
                                                  Assertions.assertEquals(o,
                                                                          supplier1.get()
                                                                                   .get(p)
                                                                         );
                                                  return addSizeFn.apply(p,
                                                                         o
                                                                        );
                                              },
                                              (p, o) -> o.isNotEmpty()
                                             );

        Assertions.assertEquals(arr1,
                                newArr1
                               );

    }

    @Test
    void test_map_json_immutable_array() throws MalformedJson
    {
        final JsArray arr = JsArray.of(JsObj.of("a",
                                                JsObj.empty(),
                                                "b",
                                                JsStr.of("B")
                                               ),
                                       NULL,
                                       JsObj.empty(),
                                       JsArray.empty(),
                                       JsObj.of("c",
                                                JsStr.of("C"),
                                                "d",
                                                JsStr.of("D"),
                                                "e",
                                                JsObj.of("f",
                                                         JsStr.of("F")
                                                        )
                                               )
                                      );

        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put("size",
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

        Assertions.assertEquals(Json.parse("[{\"size\":2,\"a\":{},\"b\":\"B\"},null,{},[],{\"e\":{\"f\":\"F\"},\"size\":3,\"c\":\"C\",\"d\":\"D\"}]\n")
                                    .arrOrElseThrow(),
                                newArr
                               );

        final JsArray arr1 = JsArray.of(JsObj.of("a",
                                                 JsObj.empty(),
                                                 "b",
                                                 JsStr.of("B")
                                                ),
                                        NULL,
                                        JsObj.empty(),
                                        JsArray.empty(),
                                        JsObj.of("c",
                                                 JsArray.empty(),
                                                 "d",
                                                 JsStr.of("D"),
                                                 "e",
                                                 JsObj.of("f",
                                                          JsStr.of("F"),
                                                          "g",
                                                          JsObj.of("h",
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

        Assertions.assertEquals(Json.parse("[{\"size\":2,\"a\":{},\"b\":\"B\"},null,{},[],{\"e\":{\"size\":2,\"f\":\"F\",\"g\":{\"size\":1,\"h\":\"H\"}},\"size\":3,\"c\":[],\"d\":\"D\"}]\n")
                                    .arrOrElseThrow(),
                                newArr1
                               );
    }


    @Test
    void test_operations_immutable()
    {

        JsArray array = JsArray.of(JsPair.of("0.b.0",
                                             JsInt.of(1)
                                            )
                                  );

        Assertions.assertEquals(array,
                                array.remove("0.b.c")
                               );

        Assertions.assertEquals(array,
                                array.remove("0.0.c")
                               );

        Assertions.assertEquals(array,
                                array.remove("0.b.0.a")
                               );
    }


    @Test
    void test_operations_mutable()
    {

        JsArray array = JsArray._of_(JsPair.of("0.b.0",
                                               JsInt.of(1)
                                              )
                                    );

        Assertions.assertEquals(array,
                                array.remove("0.b.c")
                               );

        Assertions.assertEquals(array,
                                array.remove("0.0.c")
                               );

        Assertions.assertEquals(array,
                                array.remove("0.b.0.a")
                               );
    }

    @Test
    void test_append_front_immutable()
    {

        final JsArray array = JsArray.empty()
                                     .prepend(JsInt.of(1))
                                     .prepend(JsStr.of("a"));

        Assertions.assertEquals(array.get("0"),
                                JsStr.of("a")
                               );

        Assertions.assertEquals(array.get("01"),
                                JsInt.of(1)
                               );
    }

    @Test
    void test_append_front_mutable()
    {

        final JsArray array = JsArray._empty_()
                                     .prepend(JsInt.of(1))
                                     .prepend(JsStr.of("a"));

        Assertions.assertEquals(array.get("0"),
                                JsStr.of("a")
                               );

        Assertions.assertEquals(array.get("01"),
                                JsInt.of(1)
                               );
    }

    @Test
    void test_parse_into_immutable() throws MalformedJson
    {
        JsArray arr = JsArray.of(JsPair.of("0.b.0",
                                           NULL
                                          ),
                                 JsPair.of("0.b.1",
                                           TRUE
                                          ),
                                 JsPair.of("0.b.c",
                                           FALSE
                                          ),
                                 JsPair.of("1.b.c.d",
                                           JsInt.of(1)
                                          ),
                                 JsPair.of("1.a.a",
                                           JsStr.of("a")
                                          ),
                                 JsPair.of("1.b.0",
                                           JsBigDec.of(BigDecimal.ONE)
                                          ),
                                 JsPair.of("1.b.1",
                                           NULL
                                          )
                                );

        Assertions.assertEquals(arr,
                                JsArray.parse(arr.toString())
                                       .orElseThrow()
                               );

        Assertions.assertEquals(arr,
                                Json.parse(arr.toString())
                                    .arrOrElseThrow()
                               );

    }

    @Test
    void test_parse_into_mutable() throws MalformedJson
    {
        JsArray arr = JsArray._of_(JsPair.of("0.b.0",
                                             NULL
                                            ),
                                   JsPair.of("0.b.1",
                                             TRUE
                                            ),
                                   JsPair.of("0.b.c",
                                             FALSE
                                            ),
                                   JsPair.of("1.b.c.d",
                                             JsInt.of(1)
                                            ),
                                   JsPair.of("1.a.a",
                                             JsStr.of("a")
                                            ),
                                   JsPair.of("1.b.0",
                                             JsBigDec.of(BigDecimal.ONE)
                                            ),
                                   JsPair.of("1.b.1",
                                             NULL
                                            )
                                  );

        Assertions.assertEquals(arr,
                                JsArray._parse_(arr.toString())
                                       .orElseThrow()
                               );

        Assertions.assertEquals(arr,
                                Json._parse_(arr.toString())
                                    .arrOrElseThrow()
                               );

    }

    @Test
    void test_parse_malformed_string_into_mutable_fails()
    {

        Assertions.assertTrue(Json._parse_("[")
                                  .isFailure());
    }



    @Test
    void equals_arr_of_str()
    {

        final JsArray arr = JsArray.of("a",
                                       "b",
                                       "c",
                                       "a",
                                       "c"
                                      );

        final JsArray arr2 = JsArray.of("a",
                                        "b",
                                        "c"
                                       );

        final JsArray arr3 = JsArray.of("a",
                                        "a",
                                        "b",
                                        "c",
                                        "c"
                                       );

        Assertions.assertTrue(arr.equals(arr,
                                         LIST
                                        ));
        Assertions.assertTrue(arr.equals(arr2,
                                         SET
                                        ));
        Assertions.assertTrue(arr.equals(arr3,
                                         MULTISET
                                        ));


    }

    @Test
    void equals_arr_of_obj()
    {

        final JsArray arr = JsArray.of(JsObj.of("a",
                                                NULL
                                               ),
                                       JsObj.of("b",
                                                NULL
                                               ),
                                       JsObj.of("c",
                                                NULL
                                               ),
                                       JsObj.of("a",
                                                NULL
                                               ),
                                       JsObj.of("c",
                                                NULL
                                               )
                                      );

        final JsArray arr2 = JsArray.of(JsObj.of("a",
                                                 NULL
                                                ),
                                        JsObj.of("b",
                                                 NULL
                                                ),
                                        JsObj.of("c",
                                                 NULL
                                                )
                                       );

        final JsArray arr3 = JsArray.of(JsObj.of("a",
                                                 NULL
                                                ),
                                        JsObj.of("a",
                                                 NULL
                                                ),
                                        JsObj.of("b",
                                                 NULL
                                                ),
                                        JsObj.of("c",
                                                 NULL
                                                ),
                                        JsObj.of("c",
                                                 NULL
                                                )
                                       );

        Assertions.assertTrue(arr.equals(arr,
                                         LIST
                                        ));
        Assertions.assertTrue(arr.equals(arr2,
                                         SET
                                        ));
        Assertions.assertTrue(arr.equals(arr3,
                                         MULTISET
                                        ));


    }

    @Test
    void filter_keys_immutable() throws MalformedJson
    {
        final JsArray arr = JsArray.of(NULL,
                                       TRUE,
                                       FALSE,
                                       JsObj.of("a",
                                                NULL,
                                                "b",
                                                NULL,
                                                "c",
                                                TRUE
                                               ),
                                       JsObj.of("a",
                                                NULL,
                                                "b",
                                                NULL,
                                                "c",
                                                TRUE,
                                                "d",
                                                JsArray.of(NULL,
                                                           JsObj.of("a",
                                                                    NULL,
                                                                    "b",
                                                                    TRUE
                                                                   )
                                                          )
                                               )
                                      );

        final JsArray arr1 = arr.filterKeys_(p -> p.elem.isNotNull());

        Assertions.assertEquals(JsArray.parse("[null,true,false,{\"c\":true},{\"c\":true,\"d\":[null,{\"b\":true}]}]\n")
                                       .orElseThrow(),
                                arr1
                               );
    }

    @Test
    void test_contains_element_in_array()
    {
        final JsArray arr = JsArray.of(JsInt.of(1),
                                       JsArray.of(JsInt.of(2),
                                                  JsObj.of("a",
                                                           NULL
                                                          )
                                                 )
                                      );
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            JsElem next = iterator.next();
            Assertions.assertTrue(arr.containsElem(next));
        }
        final JsArray _arr_ = JsArray._of_(JsInt.of(1),
                                           JsArray._of_(JsInt.of(2),
                                                        JsObj._of_("a",
                                                                   NULL
                                                                  )
                                                       )
                                          );

        final Iterator<JsElem> iterator1 = _arr_.iterator();
        while (iterator1.hasNext())
        {
            JsElem next = iterator1.next();
            Assertions.assertTrue(_arr_.containsElem(next));

        }
        Assertions.assertTrue(arr.containsElem(JsInt.of(1)));
        Assertions.assertTrue(_arr_.containsElem(JsInt.of(1)));
        Assertions.assertFalse(arr.containsElem(JsInt.of(2)));
        Assertions.assertFalse(_arr_.containsElem(JsInt.of(2)));
        Assertions.assertTrue(arr.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(_arr_.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(arr.containsElem_(NULL));
        Assertions.assertTrue(_arr_.containsElem_(NULL));
        Assertions.assertTrue(arr.containsPath("1.1.a"));
        Assertions.assertTrue(_arr_.containsPath("1.1.a"));
        Assertions.assertTrue(arr.containsPath("1"));
        Assertions.assertTrue(_arr_.containsPath("1"));
        Assertions.assertTrue(arr.containsPath("0"));
        Assertions.assertTrue(_arr_.containsPath("0"));
        Assertions.assertFalse(arr.containsPath("3"));
        Assertions.assertFalse(_arr_.containsPath("3"));
        Assertions.assertFalse(arr.containsPath("1.b"));
        Assertions.assertFalse(_arr_.containsPath("1.b"));
    }


}
