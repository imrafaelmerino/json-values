package jsonvalues;

import com.dslplatform.json.JsParserException;
import jsonvalues.spec.Error;
import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

import static jsonvalues.Functions.assertErrorIs;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.spec.ERROR_CODE.*;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjSpec {


    @Test
    public void testIsStrSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                str
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsInt.of(1)
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                STRING_EXPECTED
                               );

        Assertions.assertEquals(pair.error.value,
                                JsInt.of(1)
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );

    }

    @Test
    public void testIsStrPredicateSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                str(s -> s.startsWith("h"))
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("bye")
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      STRING_CONDITION,
                      JsStr.of("bye"),
                      JsPath.fromKey("a")
                     );

    }


    @Test
    public void testIsIntSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                integer
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("a")
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      INT_EXPECTED,
                      JsStr.of("a"),
                      JsPath.fromKey("a")
                     );


    }

    @Test
    public void testIsIntPredicateSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                integer(n -> n % 2 == 0)
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsInt.of(5)
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                INT_CONDITION
                               );

        Assertions.assertEquals(pair.error.value,
                                JsInt.of(5)
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsLongSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                longInteger
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("a")
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      LONG_EXPECTED,
                      JsStr.of("a"),
                      JsPath.fromKey("a")
                     );


    }

    @Test
    public void testIsLongPredicateSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                longInteger(l -> l % 2 == 1)
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsLong.of(4)
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                LONG_CONDITION
                               );

        Assertions.assertEquals(pair.error.value,
                                JsLong.of(4)
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsDecimalSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                decimal
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("a")
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      DECIMAL_EXPECTED,
                      JsStr.of("a"),
                      JsPath.fromKey("a")
                     );


    }

    @Test
    public void testIsDecimalSpecPredicate() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                decimal(d -> d.longValueExact() == Long.MAX_VALUE)
                                               );


        final JsBigDec bd = JsBigDec.of(new BigDecimal(Long.MAX_VALUE - 1));
        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          bd
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                DECIMAL_CONDITION
                               );

        Assertions.assertEquals(pair.error.value,
                                bd
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsBooleanSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                bool
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("a")
                                                         )
                                                );

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      BOOLEAN_EXPECTED,
                      JsStr.of("a"),
                      JsPath.fromKey("a")
                     );


    }

    @Test
    public void testIsTrueSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                JsSpecs.TRUE
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsBool.FALSE
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                TRUE_EXPECTED
                               );

        Assertions.assertEquals(pair.error.value,
                                JsBool.FALSE
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsFalseSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                FALSE
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          TRUE
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                FALSE_EXPECTED
                               );

        Assertions.assertEquals(pair.error.value,
                                TRUE
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsNumberSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                integral,
                                                "b",
                                                integral,
                                                "c",
                                                integral,
                                                "d",
                                                number
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsInt.of(1),
                                                          "b",
                                                          JsLong.of(2),
                                                          "c",
                                                          JsBigInt.of(new BigInteger("100")),
                                                          "d",
                                                          JsDouble.of(3.2)
                                                         )
                                                );

        Assertions.assertTrue(error.isEmpty());


    }

    @Test
    public void testIsNumberSpecReturnError() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                integral
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("a")
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      INTEGRAL_EXPECTED,
                      JsStr.of("a"),
                      JsPath.fromKey("a")
                     );


    }

    @Test
    public void testIsObjectSpecReturnError() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                obj
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("a")
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      OBJ_EXPECTED,
                      JsStr.of("a"),
                      JsPath.fromKey("a")
                     );


    }

    @Test
    public void testIsArraySpecReturnError() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                array
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsStr.of("a")
                                                         ));

        Assertions.assertFalse(error.isEmpty());

        assertErrorIs(error,
                      ARRAY_EXPECTED,
                      JsStr.of("a"),
                      JsPath.fromKey("a")
                     );


    }

    @Test
    public void testIsObjSpec() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                integer,
                                                "b",
                                                str,
                                                "c",
                                                longInteger,
                                                "d",
                                                bool,
                                                "e",
                                                JsSpecs.TRUE,
                                                "f",
                                                JsObjSpec.strict("a",
                                                                 str,
                                                                 "b",
                                                                 integer,
                                                                 "c",
                                                                 JsSpecs.tuple(str,
                                                                               integer
                                                                              ),
                                                                 "d",
                                                                 arraySuchThat(a -> a.head() == JsNull.NULL)
                                                                ),
                                                "g",
                                                JsSpecs.oneOf(Arrays.asList(JsStr.of("A"),
                                                                            JsStr.of("B")
                                                                           ))
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsInt.of(1),
                                                          "b",
                                                          JsStr.of("hi"),
                                                          "c",
                                                          JsLong.of(100),
                                                          "d",
                                                          TRUE,
                                                          "e",
                                                          TRUE,
                                                          "f",
                                                          JsObj.of("a",
                                                                   JsStr.of("hi"),
                                                                   "b",
                                                                   JsInt.of(1),
                                                                   "c",
                                                                   JsArray.of(JsStr.of("a"),
                                                                              JsInt.of(1)
                                                                             ),
                                                                   "d",
                                                                   JsArray.of(JsNull.NULL,
                                                                              JsInt.of(1)
                                                                             )
                                                                  ),
                                                          "g",
                                                          JsStr.of("A")
                                                         )
                                                );

        Assertions.assertTrue(error.isEmpty());


    }


    @Test
    public void testIsArrayOfPrimitivesSpecs() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                arrayOfInt,
                                                "b",
                                                arrayOfStr,
                                                "c",
                                                arrayOfLong,
                                                "d",
                                                arrayOfDec,
                                                "e",
                                                arrayOfIntegral,
                                                "f",
                                                JsObjSpec.strict("a",
                                                                 arrayOfNumber,
                                                                 "b",
                                                                 arrayOfObj,
                                                                 "c",
                                                                 arrayOfBool
                                                                )
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsArray.of(1,
                                                                     2,
                                                                     3
                                                                    ),
                                                          "b",
                                                          JsArray.of("a",
                                                                     "b"
                                                                    ),
                                                          "c",
                                                          JsArray.of(100L,
                                                                     200L
                                                                    ),
                                                          "d",
                                                          JsArray.of(1.2,
                                                                     1.3
                                                                    ),
                                                          "e",
                                                          JsArray.of(BigInteger.TEN,
                                                                     BigInteger.TEN
                                                                    ),
                                                          "f",
                                                          JsObj.of("a",
                                                                   JsArray.of(JsInt.of(1),
                                                                              JsDouble.of(1.5)
                                                                             ),
                                                                   "b",
                                                                   JsArray.of(JsObj.empty(),
                                                                              JsObj.empty()
                                                                             ),
                                                                   "c",
                                                                   JsArray.of(true,
                                                                              false,
                                                                              true
                                                                             )
                                                                  )
                                                         )
                                                );


        Assertions.assertTrue(error.isEmpty());


    }


    @Test
    public void testIsArrayOfTestedPrimitivesSpecs() {

        final JsObjSpec spec = JsObjSpec.strict("a",
                                                arrayOfInt(i -> i > 0).optional()
                                                                      .nullable(),
                                                "b",
                                                arrayOfStr(s -> s.startsWith("a")),
                                                "c",
                                                arrayOfLong(i -> i < 0).optional()
                                                                       .nullable(),
                                                "c1",
                                                arrayOfLong(i -> i < 0).nullable(),
                                                "c2",
                                                arrayOfLong(i -> i < 0).nullable(),
                                                "d",
                                                arrayOfDec(b -> b.doubleValue() > 1.5),
                                                "e",
                                                arrayOfIntegral(i -> i.longValue() < 100),
                                                "f",
                                                JsObjSpec.strict("a",
                                                                 arrayOfNumber(JsValue::isInt),
                                                                 "b",
                                                                 arrayOfObj(JsObj::isEmpty).optional()
                                                                                           .nullable(),
                                                                 "c",
                                                                 JsSpecs.tuple(arrayOfStrSuchThat(a -> a.size() > 2),
                                                                               arrayOfIntSuchThat(a -> a.size() > 1),
                                                                               arrayOfLongSuchThat(a -> a.containsValue(JsLong.of(10))),
                                                                               arrayOfDecSuchThat(a -> a.size() == 1)
                                                                              ),
                                                                 "d",
                                                                 integral(i -> i.longValue() > 10),
                                                                 "e",
                                                                 number(JsValue::isDouble),
                                                                 "f",
                                                                 obj(JsObj::isEmpty)
                                                                )
                                               );


        final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                          JsArray.of(1,
                                                                     2,
                                                                     3
                                                                    ),
                                                          "b",
                                                          JsArray.of("a",
                                                                     "abc"
                                                                    ),
                                                          "c",
                                                          JsArray.of(-100L,
                                                                     -200L
                                                                    ),
                                                          "c1",
                                                          JsNull.NULL,
                                                          "c2",
                                                          JsArray.of(-1L,
                                                                     -2L
                                                                    ),
                                                          "d",
                                                          JsArray.of(2.0,
                                                                     3.0
                                                                    ),
                                                          "e",
                                                          JsArray.of(BigInteger.TEN,
                                                                     BigInteger.TEN
                                                                    ),
                                                          "f",
                                                          JsObj.of("a",
                                                                   JsArray.of(JsInt.of(1),
                                                                              JsInt.of(5)
                                                                             ),
                                                                   "b",
                                                                   JsArray.of(JsObj.empty(),
                                                                              JsObj.empty()
                                                                             ),
                                                                   "c",
                                                                   JsArray.of(JsArray.of("a",
                                                                                         "b",
                                                                                         "c"
                                                                                        ),
                                                                              JsArray.of(1,
                                                                                         2
                                                                                        ),
                                                                              JsArray.of(10L),
                                                                              JsArray.of(1.2)
                                                                             ),
                                                                   "d",
                                                                   JsInt.of(11),
                                                                   "e",
                                                                   JsDouble.of(2.5),
                                                                   "f",
                                                                   JsObj.empty()
                                                                  )
                                                         )
                                                );

        Assertions.assertTrue(error.isEmpty());


    }

    @Test
    public void test_value_without_spec_should_return_error() {

        final JsObj obj = JsObj.of("a",
                                   JsInt.of(1),
                                   "b",
                                   JsStr.of("a")
                                  );
        final JsObjSpec spec = JsObjSpec.strict("a",
                                                integer
                                               );
        final Set<JsErrorPair> error = spec.test(obj);

        assertErrorIs(error,
                      SPEC_MISSING,
                      JsStr.of("a"),
                      JsPath.fromKey("b")
                     );

    }


    @Test
    public void test_any_spec() {


        JsObjSpec spec = JsObjSpec.strict("a",
                                          any,
                                          "b",
                                          any.optional(),
                                          "d",
                                          any(JsValue::isStr).optional()
                                         );

        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsNull.NULL
                                                ))
                                  .isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsNull.NULL,
                                                 "b",
                                                 TRUE,
                                                 "d",
                                                 JsStr.of("hi")
                                                ))
                                  .isEmpty());
    }

    @Test
    public void test_is_object_spec() {


        JsObjSpec spec = JsObjSpec.strict("a",
                                          JsObjSpec.strict("a",
                                                           any,
                                                           "b",
                                                           any.optional(),
                                                           "d",
                                                           any(JsValue::isStr)
                                                          )
                                         );


        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsObj.of("a",
                                                          JsNull.NULL,
                                                          "b",
                                                          TRUE,
                                                          "d",
                                                          JsStr.of("hi")
                                                         )
                                                ))
                                  .isEmpty());
    }

    @Test
    public void test_is_array_spec() {


        JsObjSpec spec = JsObjSpec.strict("a",
                                          JsSpecs.tuple(any,
                                                        integer
                                                       )
                                         );


        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(
                                                         JsNull.NULL,
                                                         JsInt.of(1)
                                                           )
                                                ))
                                  .isEmpty());

        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(
                                                         JsStr.of("hi"),
                                                         JsInt.of(1)
                                                           )
                                                ))
                                  .isEmpty());
    }

    @Test
    public void testJsSpec() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          array,
                                          "c",
                                          arrayOfBool,
                                          "d",
                                          arrayOfDec,
                                          "e",
                                          arrayOfInt,
                                          "f",
                                          arrayOfLong,
                                          "g",
                                          arrayOfIntegral,
                                          "h",
                                          arrayOfNumber.optional()
                                                       .nullable(),
                                          "i",
                                          arrayOfObj,
                                          "j",
                                          arrayOfStr,
                                          "k",
                                          arrayOf(JsObjSpec.lenient("a",
                                                                    bool,
                                                                    "b",
                                                                    str,
                                                                    "c",
                                                                    integer,
                                                                    "d",
                                                                    longInteger,
                                                                    "e",
                                                                    obj,
                                                                    "f",
                                                                    array,
                                                                    "g",
                                                                    integral,
                                                                    "h",
                                                                    decimal,
                                                                    "i",
                                                                    number
                                                                   )
                                                 )
                                         );

        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(1,
                                                            2
                                                           ),
                                                 "c",
                                                 JsArray.of(true,
                                                            false
                                                           ),
                                                 "d",
                                                 JsArray.of(1.1,
                                                            1.2
                                                           ),
                                                 "e",
                                                 JsArray.of(1,
                                                            2,
                                                            3
                                                           ),
                                                 "f",
                                                 JsArray.of(1L,
                                                            2L
                                                           ),
                                                 "g",
                                                 JsArray.of(JsInt.of(1),
                                                            JsLong.of(1L),
                                                            JsBigInt.of(BigInteger.TEN)
                                                           ),
                                                 "h",
                                                 JsArray.of(JsInt.of(1),
                                                            JsLong.of(1L),
                                                            JsBigInt.of(BigInteger.TEN),
                                                            JsBigDec.of(BigDecimal.TEN)
                                                           ),
                                                 "i",
                                                 JsArray.of(JsObj.empty(),
                                                            JsObj.empty()
                                                           ),
                                                 "j",
                                                 JsArray.of("a",
                                                            "b"
                                                           ),
                                                 "k",
                                                 JsArray.of(JsObj.of("a",
                                                                     TRUE,
                                                                     "b",
                                                                     JsStr.of("a"),
                                                                     "c",
                                                                     JsInt.of(1),
                                                                     "d",
                                                                     JsLong.of(1L),
                                                                     "e",
                                                                     JsObj.empty(),
                                                                     "f",
                                                                     JsArray.empty(),
                                                                     "g",
                                                                     JsBigInt.of(BigInteger.TEN),
                                                                     "h",
                                                                     JsBigDec.of(BigDecimal.TEN),
                                                                     "i",
                                                                     JsLong.of(1L)
                                                                    )
                                                           )
                                                ))
                                  .isEmpty()
                             );
    }

    @Test
    public void testNullableJsSpec() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          array.nullable(),
                                          "c",
                                          arrayOfBool.nullable(),
                                          "d",
                                          arrayOfDec.nullable(),
                                          "e",
                                          arrayOfInt.nullable(),
                                          "f",
                                          arrayOfLong.nullable(),
                                          "g",
                                          arrayOfIntegral.nullable(),
                                          "h",
                                          arrayOfNumber.nullable(),
                                          "i",
                                          arrayOfObj.nullable(),
                                          "j",
                                          arrayOfStr.nullable(),
                                          "k",
                                          arrayOf(JsObjSpec.lenient("a",
                                                                    bool.nullable(),
                                                                    "b",
                                                                    str.nullable(),
                                                                    "c",
                                                                    integer.nullable(),
                                                                    "d",
                                                                    longInteger.nullable(),
                                                                    "e",
                                                                    obj.nullable(),
                                                                    "f",
                                                                    array.nullable(),
                                                                    "g",
                                                                    integral.nullable(),
                                                                    "h",
                                                                    decimal.nullable(),
                                                                    "i",
                                                                    number.nullable()
                                                                   )
                                                 ).nullable()
                                         );

        final Set<JsErrorPair> result = spec.test(JsObj.of("a",
                                                           JsNull.NULL,
                                                           "c",
                                                           JsNull.NULL,
                                                           "d",
                                                           JsNull.NULL,
                                                           "e",
                                                           JsNull.NULL,
                                                           "f",
                                                           JsNull.NULL,
                                                           "g",
                                                           JsNull.NULL,
                                                           "h",
                                                           JsNull.NULL,
                                                           "i",
                                                           JsNull.NULL,
                                                           "j",
                                                           JsNull.NULL,
                                                           "k",
                                                           JsNull.NULL
                                                          )
                                                 );


        Assertions.assertTrue(result
                                      .isEmpty()
                             );
    }

    @Test
    public void testOptionalNullableJsSpec() {

        JsObjSpec spec = JsObjSpec.lenient("a",
                                           array.nullable()
                                                .optional(),
                                           "c",
                                           arrayOfBool.nullable()
                                                      .optional(),
                                           "d",
                                           arrayOfDec.nullable()
                                                     .optional(),
                                           "e",
                                           arrayOfInt.nullable()
                                                     .optional(),
                                           "f",
                                           arrayOfLong.nullable()
                                                      .optional(),
                                           "g",
                                           arrayOfIntegral.nullable()
                                                          .optional(),
                                           "h",
                                           arrayOfNumber.nullable()
                                                        .optional(),
                                           "i",
                                           arrayOfObj.nullable()
                                                     .optional(),
                                           "j",
                                           arrayOfStr.nullable()
                                                     .optional(),
                                           "k",
                                           arrayOf(JsObjSpec.lenient("a",
                                                                     bool.nullable()
                                                                         .optional(),
                                                                     "b",
                                                                     str.optional()
                                                                        .nullable(),
                                                                     "c",
                                                                     integer.nullable()
                                                                            .optional(),
                                                                     "d",
                                                                     longInteger.nullable()
                                                                                .optional(),
                                                                     "e",
                                                                     obj.nullable()
                                                                        .optional(),
                                                                     "f",
                                                                     array.nullable()
                                                                          .optional(),
                                                                     "g",
                                                                     integral.nullable()
                                                                             .optional(),
                                                                     "h",
                                                                     decimal.nullable()
                                                                            .optional(),
                                                                     "i",
                                                                     number.nullable()
                                                                           .optional()
                                                                    )
                                                  ).nullable()
                                                   .optional()
                                          );

        final Set<JsErrorPair> result = spec.test(JsObj.of("a",
                                                           JsNull.NULL,
                                                           "c",
                                                           JsNull.NULL,
                                                           "d",
                                                           JsNull.NULL,
                                                           "e",
                                                           JsNull.NULL,
                                                           "f",
                                                           JsNull.NULL,
                                                           "g",
                                                           JsNull.NULL,
                                                           "h",
                                                           JsNull.NULL,
                                                           "i",
                                                           JsNull.NULL,
                                                           "j",
                                                           JsNull.NULL,
                                                           "k",
                                                           JsNull.NULL
                                                          )
                                                 );

        Assertions.assertTrue(result
                                      .isEmpty()
                             );


        Assertions.assertTrue(spec.test(JsObj.empty())
                                  .isEmpty());


        final Set<JsErrorPair> result1 = spec.test(JsObj.of("a",
                                                            JsNull.NULL,
                                                            "b",
                                                            JsNull.NULL,
                                                            "c",
                                                            JsNull.NULL,
                                                            "d",
                                                            JsNull.NULL,
                                                            "e",
                                                            JsNull.NULL,
                                                            "f",
                                                            JsNull.NULL,
                                                            "g",
                                                            JsNull.NULL,
                                                            "h",
                                                            JsNull.NULL,
                                                            "i",
                                                            JsNull.NULL,
                                                            "j",
                                                            JsNull.NULL,
                                                            "k",
                                                            JsArray.of(JsObj.of("a",
                                                                                JsNull.NULL,
                                                                                "b",
                                                                                JsNull.NULL,
                                                                                "c",
                                                                                JsNull.NULL,
                                                                                "d",
                                                                                JsNull.NULL,
                                                                                "e",
                                                                                JsNull.NULL,
                                                                                "f",
                                                                                JsNull.NULL,
                                                                                "g",
                                                                                JsNull.NULL,
                                                                                "h",
                                                                                JsNull.NULL,
                                                                                "i",
                                                                                JsNull.NULL
                                                                               )
                                                                      )
                                                           )
                                                  );
        Assertions.assertTrue(result1.isEmpty());
    }


    @Test
    public void testOptionalJsSpec() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          array.optional(),
                                          "c",
                                          arrayOfBool.optional(),
                                          "d",
                                          arrayOfDec.optional(),
                                          "e",
                                          arrayOfInt.optional(),
                                          "f",
                                          arrayOfLong.optional(),
                                          "g",
                                          arrayOfIntegral.optional(),
                                          "h",
                                          arrayOfNumber.optional(),
                                          "i",
                                          arrayOfObj.optional(),
                                          "j",
                                          arrayOfStr.optional(),
                                          "k",
                                          arrayOf(JsObjSpec.lenient("a",
                                                                    bool.optional(),
                                                                    "b",
                                                                    str.optional(),
                                                                    "c",
                                                                    integer.optional(),
                                                                    "d",
                                                                    longInteger.optional(),
                                                                    "e",
                                                                    obj.optional(),
                                                                    "f",
                                                                    array.optional(),
                                                                    "g",
                                                                    integral.optional(),
                                                                    "h",
                                                                    decimal.optional(),
                                                                    "i",
                                                                    number.optional()
                                                                   )
                                                 ).optional()
                                         );


        Assertions.assertTrue(spec.test(JsObj.empty())
                                  .isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("k",
                                                 JsArray.of(JsObj.empty())
                                                ))
                                  .isEmpty());
    }


    @Test
    public void testIsNullableObjSpec() {

        final JsObjSpec objSpec = JsObjSpec.lenient("a",
                                                    str
                                                   );
        JsObjSpec spec = JsObjSpec.strict("a",
                                          objSpec,
                                          "b",
                                          objSpec.optional()
                                                 .nullable(),
                                          "c",
                                          objSpec.optional(),
                                          "d",
                                          objSpec.optional()
                                                 .nullable()
                                         );

        final Set<JsErrorPair> set = spec.test(JsObj.of("a",
                                                        JsObj.of("a",
                                                                 JsStr.of("a"),
                                                                 "b",
                                                                 TRUE
                                                                ),
                                                        "b",
                                                        JsNull.NULL
                                                       ));

        Assertions.assertTrue(set.isEmpty());

    }

    @Test
    public void testArrayOfObjSpec() {

        final JsSpec a = arrayOf(JsObjSpec.lenient("a",
                                                   integral
                                                  ));
        final JsSpec b = arrayOf(JsObjSpec.lenient("a",
                                                   str
                                                  )).nullable();
        final JsSpec c = arrayOf(JsObjSpec.lenient("a",
                                                   integral
                                                  )).nullable()
                                                    .optional();
        final JsSpec d = arrayOf(JsObjSpec.lenient("a",
                                                   integral
                                                  )).optional();

        final JsObjSpec objspec = JsObjSpec.strict("a",
                                                   a,
                                                   "b",
                                                   b,
                                                   "c",
                                                   c,
                                                   "d",
                                                   d
                                                  );

        final Set<JsErrorPair> errors = objspec.test(JsObj.of("a",
                                                              JsArray.of(JsObj.of("a",
                                                                                  JsInt.of(1)
                                                                                 )),

                                                              "b",
                                                              JsNull.NULL,
                                                              "d",
                                                              JsArray.of(JsObj.of("a",
                                                                                  JsInt.of(1)
                                                                                 ))
                                                             )
                                                    );

        Assertions.assertTrue(errors
                                      .isEmpty()
                             );

    }

    @Test
    public void test_is_array_of_obspec() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          str,
                                          "b",
                                          integral
                                         );
        JsObjSpec b = JsObjSpec.strict("a",
                                       arrayOf(spec),
                                       "b",
                                       arrayOf(spec).nullable(),
                                       "c",
                                       arrayOf(spec).optional(),
                                       "d",
                                       arrayOf(spec).nullable()
                                                    .optional()
                                      );

        Assertions.assertTrue(b.test(JsObj.of("a",
                                              JsArray.of(JsObj.of("a",
                                                                  JsStr.of("a"),
                                                                  "b",
                                                                  JsInt.of(1)
                                                                 )
                                                        ),
                                              "b",
                                              JsNull.NULL,
                                              "d",
                                              JsNull.NULL
                                             )
                                    )
                               .isEmpty());


    }

    @Test
    public void test_required_fields() {
        final JsObjSpec spec = JsObjSpec.strict("a",
                                                str.optional(),
                                                "b",
                                                JsObjSpec.strict("c",
                                                                 integral
                                                                )
                                               );

        final Set<JsErrorPair> errors = spec.test(JsObj.of("b",
                                                           JsObj.empty()
                                                          ));

        Assertions.assertEquals(1,
                                errors.size()
                               );
        Assertions.assertEquals(JsPath.path("/b/c"),
                                errors.stream()
                                      .findFirst()
                                      .get().path
                               );

    }

    @Test
    public void test_errors_schemas() {
        final JsObjSpec spec = JsObjSpec.strict("a",
                                                JsObjSpec.strict("A",
                                                                 integer
                                                                ),
                                                "b",
                                                JsSpecs.tuple(str),
                                                "c",
                                                arrayOf(JsObjSpec.lenient("a",
                                                                          str
                                                                         )
                                                       )
                                               );

        final Set<JsErrorPair> errors = spec.test(JsObj.of("a",
                                                           JsStr.of("a"),
                                                           "b",
                                                           JsInt.of(1),
                                                           "c",
                                                           TRUE
                                                          ));

        Assertions.assertFalse(errors.isEmpty());


    }

    @Test
    public void testBinarySpec() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          binary,
                                          "b",
                                          binary
                                         );
        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsStr.of("hola"),
                                                 "b",
                                                 JsBinary.of("foo".getBytes())
                                                ))
                                  .isEmpty());

        Set<JsErrorPair> result = spec.test(JsObj.of("a",
                                                     JsStr.of("ñññ"),
                                                     "b",
                                                     JsInt.of(1)
                                                    ));
        Assertions.assertTrue(result.size() == 2);

        Assertions.assertTrue(result.stream()
                                    .anyMatch(e -> e.path.equals(JsPath.fromKey("b")) && e.error.code.equals(BINARY_EXPECTED)));

        Assertions.assertTrue(result.stream()
                                    .anyMatch(e -> e.path.equals(JsPath.fromKey("a")) && e.error.code.equals(BINARY_EXPECTED)));


    }

    @Test
    public void testInstantSpec() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          instant,
                                          "b",
                                          instant
                                         );

        Set<JsErrorPair> errorPairs = spec.test(JsObj.of("a",
                                                         JsInstant.of(Instant.now()),
                                                         "b",
                                                         JsStr.of(Instant.now()
                                                                         .toString())
                                                        ));
        Assertions.assertTrue(errorPairs
                                      .isEmpty());

        Set<JsErrorPair> errorPairs1 = spec.test(JsObj.of("a",
                                                          JsStr.of("hola"),
                                                          "b",
                                                          JsStr.of(LocalDateTime.now()
                                                                                .toString())
                                                         ));


        Assertions.assertTrue(errorPairs1.size() == 2);


        Assertions.assertTrue(errorPairs1.stream()
                                         .anyMatch(e -> e.path.equals(JsPath.fromKey("b")) && e.error.code.equals(INSTANT_EXPECTED)));

        Assertions.assertTrue(errorPairs1.stream()
                                         .anyMatch(e -> e.path.equals(JsPath.fromKey("a")) && e.error.code.equals(INSTANT_EXPECTED)));


    }

    @Test
    public void testConstantSpec() {

        Set<JsErrorPair> errorPairSet = cons(JsStr.of("hi")).test(JsPath.empty(),
                                                                  JsInt.of(1)
                                                                 );


        Assertions.assertTrue(errorPairSet.contains(JsErrorPair.of(JsPath.empty(),
                                                                   new Error(JsInt.of(1),
                                                                             CONSTANT_CONDITION
                                                                   )
                                                                  )
                                                   ));


        JsObjSpec spec = JsObjSpec.strict("a",
                                          cons(JsInt.of(2)).optional()
                                         );

        Assertions.assertTrue(spec.test(JsObj.empty())
                                  .isEmpty());
    }


    @Test
    public void test() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          longInteger,
                                          "b",
                                          integer
                                         );

        Set<JsErrorPair> errors = spec.test(JsObj.of("a",
                                                     JsStr.of("123"),
                                                     "b",
                                                     JsStr.of("234")
                                                    ));

        System.out.println(errors);
        Assertions.assertEquals(2,
                                errors.size());

    }

    @Test
    public void test_parse() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          longInteger,
                                          "b",
                                          integer
                                         );

        JsObjParser parser = new JsObjParser(spec);

        JsObj jsObj = JsObj.of("a",
                               JsStr.of("123"),
                               "b",
                               JsStr.of("234")
                              );

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(jsObj.toString())
                                );

    }
}
