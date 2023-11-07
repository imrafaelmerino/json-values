package jsonvalues.api.spec;

import jsonvalues.*;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import jsonvalues.spec.SpecError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;
import static jsonvalues.api.spec.FunTest.assertErrorIs;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsArraySpec {

    @Test
    public void test_value_without_spec_should_return_error() {

        JsArray array = JsArray.of(JsInt.of(1),
                                   JsStr.of("a")
                                  );
        var spec = JsSpecs.tuple(integer());
        List<SpecError> error = spec.test(array);
        assertErrorIs(error,
                      SPEC_MISSING,
                      JsStr.of("a"),
                      JsPath.fromIndex(1)
                     );
    }

    @Test
    public void test_any_spec_array() {


        var spec = JsSpecs.tuple(any(),
                                 any());


        Assertions.assertTrue(spec.test(JsArray.of(JsNull.NULL,
                                                   JsBool.TRUE
                                                  ))
                                  .isEmpty());
        Assertions.assertFalse(spec.test(JsArray.of(JsNull.NULL,
                                                    JsBool.TRUE,
                                                    JsBool.FALSE
                                                   ))
                                   .isEmpty());
    }

    @Test
    public void test_array_of_boolean_such_that() {
        final JsObjSpec spec = JsObjSpec.of("a",
                                            JsSpecs.arrayOfBoolSuchThat(a -> a.head() == JsBool.TRUE)
                                           );

        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(true,
                                                            false,
                                                            false
                                                           )
                                                ))
                                  .isEmpty()
                             );


        Assertions.assertFalse(spec.test(JsObj.of("a",
                                                  JsArray.of(false,
                                                             false,
                                                             false
                                                            )
                                                 ))
                                   .isEmpty()
                              );

        Assertions.assertFalse(spec.test(JsObj.of("a",
                                                  JsArray.of(true)
                                                         .append(JsLong.of(1))
                                                 ))
                                   .isEmpty()
                              );

    }

    @Test
    public void test_any_spec_array_of_two_elements() {


        var spec = JsSpecs.tuple(any(),
                                     any()
                                    );

        Assertions.assertTrue(spec.test(JsArray.of(JsBool.FALSE,
                                                   JsBool.TRUE
                                                  ))
                                  .isEmpty());
        Assertions.assertFalse(spec.test(JsArray.of(JsBool.FALSE))
                                   .isEmpty());
        Assertions.assertFalse(spec.test(JsArray.of(JsBool.FALSE,
                                                    JsBool.TRUE,
                                                    JsBool.FALSE
                                                   ))
                                   .isEmpty());
    }


    @Test
    public void test_array_of_integral_spec() {
        JsObjSpec spec = JsObjSpec.of("a",
                                      arrayOfBigIntSuchThat(a -> a.size() == 3)
                                     );

        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(JsInt.of(1),
                                                            JsLong.of(2),
                                                            JsBigInt.of(BigInteger.TEN)
                                                           )
                                                )
                                       )
                                  .isEmpty());


        Assertions.assertFalse(spec.test(JsObj.of("a",
                                                  JsArray.of(JsInt.of(1),
                                                             JsStr.of("a")
                                                            )
                                                 )
                                        )
                                   .isEmpty());


    }

    @Test
    public void test_array_of_number_spec() {
        JsObjSpec spec = JsObjSpec.of("a",
                                      arrayOfDec()
                                     );

        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(JsInt.of(1),
                                                            JsLong.of(2),
                                                            JsDouble.of(4.5),
                                                            JsBigInt.of(BigInteger.TEN),
                                                            JsBigDec.of(BigDecimal.TEN)
                                                           )
                                                )
                                       )
                                  .isEmpty());


        Assertions.assertFalse(spec.test(JsObj.of("a",
                                                  JsArray.of(JsInt.of(1),
                                                             JsStr.of("a")
                                                            )
                                                 )
                                        )
                                   .isEmpty());


    }

    @Test
    public void test_array_of_object_spec() {
        JsObjSpec spec = JsObjSpec.of("a",
                                      arrayOfObjSuchThat(a -> a.size() == 2)
                                     );

        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(JsObj.of("a",
                                                                     JsNull.NULL
                                                                    ),
                                                            JsObj.empty()
                                                           )
                                                )
                                       )
                                  .isEmpty());


        Assertions.assertFalse(spec.test(JsObj.of("a",
                                                  JsArray.of(JsObj.empty()
                                                            )
                                                 )
                                        )
                                   .isEmpty());


    }

    @Test
    public void testArrayOfObjSpec() {

        String json_str = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 21, \"latitude\": 48.858093, \"longitude\": 2.294694, \"fruits\": [ \"apple\", \"orange\", \"pear\" ], \"numbers\": [ 1, 2, 3, 4, 5, 6,"
                          + " 7, 8, 9, 10 ], \"vegetables\": [ { \"veggieName\": \"potato\", " +
                          " \"veggieLike\": true }, { \"veggieName\": \"broccoli\", \"veggieLike\": false } ]} " + "\"veggieName\": \"broccoli\", \"veggieLike\": false } ]}";


        JsObjSpec spec = JsObjSpec.of("firstName",
                                      JsSpecs.str(),
                                      "lastName",
                                      JsSpecs.str(),
                                      "age",
                                      JsSpecs.integer(i -> i >= 0),
                                      "latitude",
                                      decimal(),
                                      "longitude",
                                      decimal(),
                                      "fruits",
                                      JsSpecs.arrayOfStr(),
                                      "numbers",
                                      JsSpecs.arrayOfInt(),
                                      "vegetables",
                                      arrayOfSpec(JsObjSpec.of("veggieName",
                                                               str(),
                                                               "veggieLike",
                                                               bool()
                                                              )
                                                 )
                                     );

        Assertions.assertTrue(spec.test(JsObj.parse(json_str))
                                  .isEmpty());


    }

    @Test
    public void isArrayOfElementsTypeError() {

        Assertions.assertEquals(1,
                                JsSpecs.tuple(arrayOfDec())
                                       .test(JsArray.of(JsArray.of("1",
                                                                   "2"
                                                                  )))
                                       .size()
                               );


        Assertions.assertEquals(1,
                                JsSpecs.tuple(arrayOfInt())
                                       .test(JsArray.of(JsArray.of("a",
                                                                   "b"
                                                                  )))
                                       .size()
                               );


        Assertions.assertEquals(1,
                                JsSpecs.tuple(arrayOfLong())
                                       .test(JsArray.of(JsArray.of("a",
                                                                   "b"
                                                                  )))
                                       .size()
                               );


        Assertions.assertEquals(1,
                                JsSpecs.tuple(arrayOfObj())
                                       .test(JsArray.of(JsArray.of("a",
                                                                   "b"
                                                                  )))
                                       .size()
                               );

        Assertions.assertEquals(1,
                                JsSpecs.tuple(arrayOfStr())
                                       .test(JsArray.of(JsArray.of(true,
                                                                   false
                                                                  )))
                                       .size()
                               );

    }

    @Test
    public void test_is_array_of_object_spec() {
        JsObjSpec spec = JsObjSpec.of("a",
                                      str(),
                                      "b",
                                      integer()
                                     ).lenient();
        JsObjSpec objSpec = JsObjSpec.of("a",
                                         arrayOfSpec(spec).nullable(),
                                         "b",
                                         arrayOfSpec(spec).nullable()
                                        ).withOptKeys("b");


        Assertions.assertTrue(objSpec.test(JsObj.of("a",
                                                    JsNull.NULL
                                                   ))
                                     .isEmpty()
                             );


    }

    @Test
    public void testArrayOfDouble() {

        JsObjSpec spec = JsObjSpec.of("a", arrayOfDouble());

        JsObjSpecParser parser = JsObjSpecParser.of(spec);

        JsObj obj = JsObj.of("a", JsArray.of(1 / 2d, 1 / 4d));
        System.out.println(obj);

        Assertions.assertTrue(spec.test(obj).isEmpty());

        JsObj obj2 = parser.parse(obj.toString());

        System.out.println(obj2);

        Assertions.assertEquals(obj, obj2);
    }
}
