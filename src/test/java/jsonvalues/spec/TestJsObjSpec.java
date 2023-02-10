package jsonvalues.spec;


import fun.gen.Gen;
import jsonvalues.*;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Set;

import static jsonvalues.JsBool.TRUE;
import static jsonvalues.spec.ERROR_CODE.*;
import static jsonvalues.spec.FunTest.assertErrorIs;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjSpec {


    @Test
    public void testIsStrSpec() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            str()
                                           );


        final Set<SpecError> errors = spec.test(JsObj.of("a",
                                                         JsInt.of(1)
                                                        ));

        errors.forEach(pair -> System.out.println(pair.value + " @ " + pair.path + " doesn't not conform spec: " + pair.errorCode));

        Assertions.assertFalse(errors.isEmpty());

        final SpecError pair = errors.stream()
                                     .findFirst()
                                     .get();

        Assertions.assertEquals(pair.errorCode,
                                STRING_EXPECTED
                               );

        Assertions.assertEquals(pair.value,
                                JsInt.of(1)
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );

    }

    @Test
    public void testIsStrPredicateSpec() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            str(s -> s.startsWith("h"))
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            integer()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            integer(n -> n % 2 == 0),
                                            "b",
                                            instant(i -> i.isAfter(Instant.MAX))
                                           );


        final Set<SpecError> errors = spec.test(JsObj.of("a",
                                                         JsInt.of(5),
                                                         "b",
                                                         JsInstant.of(Instant.now())
                                                        ));

        Assertions.assertEquals(2,
                                errors.size()
                               );

        final SpecError intError = errors.stream()
                                         .filter(it -> it.value.equals(JsInt.of(5)))
                                         .findFirst().
                                         get();

        Assertions.assertEquals(intError.errorCode,
                                INT_CONDITION
                               );

        Assertions.assertEquals(intError.value,
                                JsInt.of(5)
                               );

        Assertions.assertEquals(intError.path,
                                JsPath.fromKey("a")
                               );

        final SpecError instantError = errors.stream()
                                             .filter(it -> it.path.last().isKey(a -> a.equals("b")))
                                             .findFirst().
                                             get();

        Assertions.assertEquals(instantError.errorCode,
                                INSTANT_CONDITION
                               );


        Assertions.assertEquals(instantError.path,
                                JsPath.fromKey("b")
                               );


    }

    @Test
    public void testIsLongSpec() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            longInteger()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            longInteger(l -> l % 2 == 1)
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
                                                        JsLong.of(4)
                                                       ));

        Assertions.assertFalse(error.isEmpty());

        final SpecError pair = error.stream()
                                    .findFirst()
                                    .get();

        Assertions.assertEquals(pair.errorCode,
                                LONG_CONDITION
                               );

        Assertions.assertEquals(pair.value,
                                JsLong.of(4)
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsDecimalSpec() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            decimal()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            decimal(d -> d.longValueExact() == Long.MAX_VALUE)
                                           );


        final JsBigDec bd = JsBigDec.of(new BigDecimal(Long.MAX_VALUE - 1));
        final Set<SpecError> error = spec.test(JsObj.of("a",
                                                        bd
                                                       ));

        Assertions.assertFalse(error.isEmpty());

        final SpecError pair = error.stream()
                                    .findFirst()
                                    .get();

        Assertions.assertEquals(pair.errorCode,
                                DECIMAL_CONDITION
                               );

        Assertions.assertEquals(pair.value,
                                bd
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsBooleanSpec() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            bool()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            JsSpecs.TRUE
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
                                                        JsBool.FALSE
                                                       ));

        Assertions.assertFalse(error.isEmpty());

        final SpecError pair = error.stream()
                                    .findFirst()
                                    .get();

        Assertions.assertEquals(pair.errorCode,
                                TRUE_EXPECTED
                               );

        Assertions.assertEquals(pair.value,
                                JsBool.FALSE
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsFalseSpec() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            FALSE
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
                                                        TRUE
                                                       ));

        Assertions.assertFalse(error.isEmpty());

        final SpecError pair = error.stream()
                                    .findFirst()
                                    .get();

        Assertions.assertEquals(pair.errorCode,
                                FALSE_EXPECTED
                               );

        Assertions.assertEquals(pair.value,
                                TRUE
                               );

        Assertions.assertEquals(pair.path,
                                JsPath.fromKey("a")
                               );


    }

    @Test
    public void testIsNumberSpec() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            bigInteger(),
                                            "b",
                                            bigInteger(),
                                            "c",
                                            bigInteger(),
                                            "d",
                                            number()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            bigInteger()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            obj()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            array()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            integer(),
                                            "b",
                                            str(),
                                            "c",
                                            longInteger(),
                                            "d",
                                            bool(),
                                            "e",
                                            JsSpecs.TRUE,
                                            "f",
                                            JsObjSpec.of("a",
                                                         str(),
                                                         "b",
                                                         integer(),
                                                         "c",
                                                         JsSpecs.tuple(str(),
                                                                       integer()
                                                                      ),
                                                         "d",
                                                         arraySuchThat(a -> a.head() == JsNull.NULL)
                                                        ),
                                            "g",
                                            JsSpecs.oneOf(Arrays.asList(JsStr.of("A"),
                                                                        JsStr.of("B")
                                                                       )),
                                            "h",
                                            JsSpecs.binary(it -> it.length <= 100).nullable()
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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
                                                        JsStr.of("A"),
                                                        "h",
                                                        JsBinary.of("ssddssdfsd".getBytes(StandardCharsets.UTF_8))
                                                       )
                                              );

        Assertions.assertTrue(error.isEmpty());


    }


    @Test
    public void testIsArrayOfPrimitivesSpecs() {

        final JsObjSpec spec = JsObjSpec.of("a",
                                            arrayOfInt(),
                                            "b",
                                            arrayOfStr(),
                                            "c",
                                            arrayOfLong(),
                                            "d",
                                            arrayOfDec(),
                                            "e",
                                            arrayOfBigInt(),
                                            "f",
                                            JsObjSpec.of("a",
                                                         arrayOfNumber(),
                                                         "b",
                                                         arrayOfObj(),
                                                         "c",
                                                         arrayOfBool()
                                                        )
                                           );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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

        final JsObjSpec spec = JsObjSpec.of("a",
                                            arrayOfInt(i -> i > 0).nullable(),
                                            "b",
                                            arrayOfStr(s -> s.startsWith("a")),
                                            "c",
                                            arrayOfLong(i -> i < 0).nullable(),
                                            "c1",
                                            arrayOfLong(i -> i < 0).nullable(),
                                            "c2",
                                            arrayOfLong(i -> i < 0).nullable(),
                                            "d",
                                            arrayOfDec(b -> b.doubleValue() > 1.5),
                                            "e",
                                            arrayOfBigInt(i -> i.longValue() < 100),
                                            "f",
                                            JsObjSpec.of("a",
                                                         arrayOfNumber(JsValue::isInt),
                                                         "b",
                                                         arrayOfObj(JsObj::isEmpty).nullable(),
                                                         "c",
                                                         JsSpecs.tuple(arrayOfStrSuchThat(a -> a.size() > 2),
                                                                       arrayOfIntSuchThat(a -> a.size() > 1),
                                                                       arrayOfLongSuchThat(a -> a.containsValue(JsLong.of(10))),
                                                                       arrayOfDecSuchThat(a -> a.size() == 1)
                                                                      ),
                                                         "d",
                                                         bigInteger(i -> i.longValue() > 10),
                                                         "e",
                                                         number(JsValue::isDouble),
                                                         "f",
                                                         obj(JsObj::isEmpty)
                                                        ).withOptKeys("b")
                                           ).withOptKeys("c",
                                                         "c"
                                                        );


        final Set<SpecError> error = spec.test(JsObj.of("a",
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
        final JsObjSpec spec = JsObjSpec.of("a",
                                            integer()
                                           );
        final Set<SpecError> error = spec.test(obj);

        assertErrorIs(error,
                      SPEC_MISSING,
                      JsStr.of("a"),
                      JsPath.fromKey("b")
                     );

    }


    @Test
    public void test_any_spec() {


        JsObjSpec spec = JsObjSpec.of("a",
                                      any(),
                                      "b",
                                      any(),
                                      "d",
                                      any(JsValue::isStr)
                                     ).withOptKeys("b",
                                                   "d"
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


        JsObjSpec spec = JsObjSpec.of("a",
                                      JsObjSpec.of("a",
                                                   any(),
                                                   "b",
                                                   any(),
                                                   "d",
                                                   any(JsValue::isStr)
                                                  ).withOptKeys("b")
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


        JsObjSpec spec = JsObjSpec.of("a",
                                      JsSpecs.tuple(any(),
                                                    integer()
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

        JsObjSpec spec = JsObjSpec.of("a",
                                      array(),
                                      "c",
                                      arrayOfBool(),
                                      "d",
                                      arrayOfDec(),
                                      "e",
                                      arrayOfInt(),
                                      "f",
                                      arrayOfLong(),
                                      "g",
                                      arrayOfBigInt(),
                                      "h",
                                      arrayOfNumber().nullable(),
                                      "i",
                                      arrayOfObj(),
                                      "j",
                                      arrayOfStr(),
                                      "k",
                                      arrayOfObjSpec(JsObjSpec.of("a",
                                                                  bool(),
                                                                  "b",
                                                                  str(),
                                                                  "c",
                                                                  integer(),
                                                                  "d",
                                                                  longInteger(),
                                                                  "e",
                                                                  obj(),
                                                                  "f",
                                                                  array(),
                                                                  "g",
                                                                  bigInteger(),
                                                                  "h",
                                                                  decimal(),
                                                                  "i",
                                                                  number()
                                                                 ).lenient()
                                                    )
                                     ).withOptKeys("h");

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

        JsObjSpec spec = JsObjSpec.of("a",
                                      array().nullable(),
                                      "c",
                                      arrayOfBool().nullable(),
                                      "d",
                                      arrayOfDec().nullable(),
                                      "e",
                                      arrayOfInt().nullable(),
                                      "f",
                                      arrayOfLong().nullable(),
                                      "g",
                                      arrayOfBigInt().nullable(),
                                      "h",
                                      arrayOfNumber().nullable(),
                                      "i",
                                      arrayOfObj().nullable(),
                                      "j",
                                      arrayOfStr().nullable(),
                                      "k",
                                      arrayOfObjSpec(JsObjSpec.of("a",
                                                                  bool().nullable(),
                                                                  "b",
                                                                  str().nullable(),
                                                                  "c",
                                                                  integer().nullable(),
                                                                  "d",
                                                                  longInteger().nullable(),
                                                                  "e",
                                                                  obj().nullable(),
                                                                  "f",
                                                                  array().nullable(),
                                                                  "g",
                                                                  bigInteger().nullable(),
                                                                  "h",
                                                                  decimal().nullable(),
                                                                  "i",
                                                                  number().nullable()
                                                                 ).lenient()
                                                    ).nullable()
                                     );

        final Set<SpecError> result = spec.test(JsObj.of("a",
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

        JsObjSpec spec = JsObjSpec.of("a",
                                      array().nullable()
                ,
                                      "c",
                                      arrayOfBool().nullable()
                ,
                                      "d",
                                      arrayOfDec().nullable()
                ,
                                      "e",
                                      arrayOfInt().nullable()
                ,
                                      "f",
                                      arrayOfLong().nullable()
                ,
                                      "g",
                                      arrayOfBigInt().nullable()
                ,
                                      "h",
                                      arrayOfNumber().nullable()
                ,
                                      "i",
                                      arrayOfObj().nullable()
                ,
                                      "j",
                                      arrayOfStr().nullable(),
                                      "k",
                                      arrayOfObjSpec(JsObjSpec.of("a",
                                                                  bool().nullable(),
                                                                  "b",
                                                                  str().nullable(),
                                                                  "c",
                                                                  integer().nullable(),
                                                                  "d",
                                                                  longInteger().nullable(),
                                                                  "e",
                                                                  obj().nullable(),
                                                                  "f",
                                                                  array().nullable(),
                                                                  "g",
                                                                  bigInteger().nullable(),
                                                                  "h",
                                                                  decimal().nullable(),
                                                                  "i",
                                                                  number().nullable()
                                                                 ).lenient().withOptKeys("a",
                                                                                         "b",
                                                                                         "c",
                                                                                         "d",
                                                                                         "e",
                                                                                         "f",
                                                                                         "g",
                                                                                         "h",
                                                                                         "i"
                                                                                        )
                                                    ).nullable()
                                     ).withOptKeys("a",
                                                   "c",
                                                   "d",
                                                   "e",
                                                   "f",
                                                   "g",
                                                   "h",
                                                   "i",
                                                   "j",
                                                   "k"
                                                  ).lenient();

        final Set<SpecError> result = spec.test(JsObj.of("a",
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


        final Set<SpecError> result1 = spec.test(JsObj.of("a",
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

        JsObjSpec spec = JsObjSpec.of("a",
                                      array(),
                                      "c",
                                      arrayOfBool(),
                                      "d",
                                      arrayOfDec(),
                                      "e",
                                      arrayOfInt(),
                                      "f",
                                      arrayOfLong(),
                                      "g",
                                      arrayOfBigInt(),
                                      "h",
                                      arrayOfNumber(),
                                      "i",
                                      arrayOfObj(),
                                      "j",
                                      arrayOfStr(),
                                      "k",
                                      arrayOfObjSpec(JsObjSpec.of("a",
                                                                  bool(),
                                                                  "b",
                                                                  str(),
                                                                  "c",
                                                                  integer(),
                                                                  "d",
                                                                  longInteger(),
                                                                  "e",
                                                                  obj(),
                                                                  "f",
                                                                  array(),
                                                                  "g",
                                                                  bigInteger(),
                                                                  "h",
                                                                  decimal(),
                                                                  "i",
                                                                  number()
                                                                 ).lenient().withOptKeys("a",
                                                                                         "b",
                                                                                         "c",
                                                                                         "d",
                                                                                         "e",
                                                                                         "f",
                                                                                         "g",
                                                                                         "h",
                                                                                         "i"
                                                                                        )
                                                    )
                                     ).withOptKeys("a",
                                                   "c",
                                                   "d",
                                                   "e",
                                                   "f",
                                                   "g",
                                                   "h",
                                                   "i",
                                                   "j",
                                                   "k"
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

        final JsObjSpec objSpec = JsObjSpec.of("a",
                                               str()
                                              ).lenient();
        JsObjSpec spec = JsObjSpec.of("a",
                                      objSpec,
                                      "b",
                                      objSpec
                                              .nullable(),
                                      "c",
                                      objSpec,
                                      "d",
                                      objSpec
                                              .nullable()
                                     ).withOptKeys("b",
                                                   "c",
                                                   "d"
                                                  );

        final Set<SpecError> set = spec.test(JsObj.of("a",
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

        final JsSpec a = arrayOfObjSpec(JsObjSpec.of("a",
                                                     bigInteger()
                                                    ).lenient());
        final JsSpec b = arrayOfObjSpec(JsObjSpec.of("a",
                                                     str()
                                                    ).lenient()).nullable();
        final JsSpec c = arrayOfObjSpec(JsObjSpec.of("a",
                                                     bigInteger()
                                                    ).lenient()).nullable();
        final JsSpec d = arrayOfObjSpec(JsObjSpec.of("a",
                                                     bigInteger()
                                                    ).lenient());

        final JsObjSpec objspec = JsObjSpec.of("a",
                                               a,
                                               "b",
                                               b,
                                               "c",
                                               c,
                                               "d",
                                               d
                                              ).withOptKeys("c",
                                                            "d"
                                                           );

        final Set<SpecError> errors = objspec.test(JsObj.of("a",
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

        JsObjSpec spec = JsObjSpec.of("a",
                                      str(),
                                      "b",
                                      bigInteger()
                                     );
        JsObjSpec b = JsObjSpec.of("a",
                                   arrayOfObjSpec(spec),
                                   "b",
                                   arrayOfObjSpec(spec).nullable(),
                                   "c",
                                   arrayOfObjSpec(spec),
                                   "d",
                                   arrayOfObjSpec(spec).nullable()

                                  ).withOptKeys("c",
                                                "d"
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
        final JsObjSpec spec = JsObjSpec.of("a",
                                            str(),
                                            "b",
                                            JsObjSpec.of("c",
                                                         bigInteger()
                                                        )
                                           ).withOptKeys("a");

        final Set<SpecError> errors = spec.test(JsObj.of("b",
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
        final JsObjSpec spec = JsObjSpec.of("a",
                                            JsObjSpec.of("A",
                                                         integer()
                                                        ),
                                            "b",
                                            JsSpecs.tuple(str()),
                                            "c",
                                            arrayOfObjSpec(JsObjSpec.of("a",
                                                                        str()
                                                                       ).lenient()
                                                          )
                                           );

        final Set<SpecError> errors = spec.test(JsObj.of("a",
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

        JsObjSpec spec = JsObjSpec.of("a",
                                      binary(),
                                      "b",
                                      binary()
                                     );
        Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsStr.of("hola"),
                                                 "b",
                                                 JsBinary.of("foo".getBytes(StandardCharsets.UTF_8))
                                                ))
                                  .isEmpty());

        Set<SpecError> result = spec.test(JsObj.of("a",
                                                   JsStr.of("ñññ"),
                                                   "b",
                                                   JsInt.of(1)
                                                  ));
        Assertions.assertEquals(2,
                                result.size()
                               );

        Assertions.assertTrue(result.stream()
                                    .anyMatch(e -> e.path.equals(JsPath.fromKey("b")) && e.errorCode.equals(BINARY_EXPECTED)));

        Assertions.assertTrue(result.stream()
                                    .anyMatch(e -> e.path.equals(JsPath.fromKey("a")) && e.errorCode.equals(BINARY_EXPECTED)));


    }

    @Test
    public void testInstantSpec() {

        JsObjSpec spec = JsObjSpec.of("a",
                                      instant(),
                                      "b",
                                      instant()
                                     );

        Set<SpecError> errorPairs = spec.test(JsObj.of("a",
                                                       JsInstant.of(Instant.now()),
                                                       "b",
                                                       JsStr.of(Instant.now()
                                                                       .toString())
                                                      ));
        Assertions.assertTrue(errorPairs
                                      .isEmpty());

        Set<SpecError> errorPairs1 = spec.test(JsObj.of("a",
                                                        JsStr.of("hola"),
                                                        "b",
                                                        JsStr.of(LocalDateTime.now(ZoneId.systemDefault()).toString()
                                                                )
                                                       ));


        Assertions.assertEquals(2,
                                errorPairs1.size()
                               );


        Assertions.assertTrue(errorPairs1.stream()
                                         .anyMatch(e -> e.path.equals(JsPath.fromKey("b")) && e.errorCode.equals(INSTANT_EXPECTED)));

        Assertions.assertTrue(errorPairs1.stream()
                                         .anyMatch(e -> e.path.equals(JsPath.fromKey("a")) && e.errorCode.equals(INSTANT_EXPECTED)));


    }

    @Test
    public void testConstantSpec() {

        Set<SpecError> errorPairSet = cons(JsStr.of("hi")).test(JsPath.empty(),
                                                                JsInt.of(1)
                                                               );


        Assertions.assertTrue(errorPairSet.stream().map(it -> it.errorCode)
                                          .toList()
                                          .contains(CONSTANT_CONDITION));

        Assertions.assertTrue(errorPairSet.stream().map(it -> it.value)
                                          .toList()
                                          .contains(JsInt.of(1)));


        JsObjSpec spec = JsObjSpec.of("a",
                                      cons(JsInt.of(2))
                                     ).withOptKeys("a");

        Assertions.assertTrue(spec.test(JsObj.empty())
                                  .isEmpty());
    }


    @Test
    public void test() {

        JsObjSpec spec = JsObjSpec.of("a",
                                      longInteger(),
                                      "b",
                                      integer()
                                     );

        Set<SpecError> errors = spec.test(JsObj.of("a",
                                                   JsStr.of("123"),
                                                   "b",
                                                   JsStr.of("234")
                                                  ));

        Assertions.assertEquals(2,
                                errors.size()
                               );

    }

    @Test
    public void test_parse() {

        JsObjSpec spec = JsObjSpec.of("a",
                                      longInteger(),
                                      "b",
                                      integer()
                                     );

        JsObjSpecParser parser = new JsObjSpecParser(spec);

        JsObj jsObj = JsObj.of("a",
                               JsStr.of("123"),
                               "b",
                               JsStr.of("234")
                              );

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(jsObj.toString())
                               );

    }


    @Test
    public void testSuchThat() {

        JsObjSpec baseSpec = JsObjSpec.of("a",
                                          str(),
                                          "b",
                                          integer(),
                                          "c",
                                          str(),
                                          "d",
                                          integer(),
                                          "e",
                                          longInteger()
                                         );

        JsObjGen baseGen = JsObjGen.of("a",
                                       JsStrGen.alphabetic(),
                                       "b",
                                       JsIntGen.arbitrary(),
                                       "c",
                                       JsStrGen.alphabetic(),
                                       "d",
                                       JsIntGen.arbitrary(),
                                       "e",
                                       JsLongGen.arbitrary()
                                      );
        Gen<JsObj> gen =
                baseGen
                        .withOptKeys("a",
                                     "b",
                                     "c",
                                     "d"
                                    )
                        .suchThat(o -> dependencies(o));

        JsObjSpec spec =
                baseSpec
                        .withOptKeys("a",
                                     "b",
                                     "c",
                                     "d"
                                    )
                        .suchThat(o -> dependencies(o));

        Assertions.assertTrue(gen.sample(10000).allMatch(o -> {
            Set<SpecError> errors = spec.test(o);
            if (!errors.isEmpty()) System.out.println(errors);
            return errors.isEmpty();
        }));


        Assertions.assertTrue(baseGen.suchThat(spec).sample(10000).allMatch(o -> {
            Set<SpecError> errors = spec.test(o);
            if (!errors.isEmpty()) System.out.println(errors);
            return errors.isEmpty();
        }));


        JsObjSpec spec1 = baseSpec.withAllOptKeys().suchThat(o -> dependencies(o));
        Assertions.assertTrue(baseGen.withAllOptKeys().suchThat(spec1).sample(10000).allMatch(o -> {
            Set<SpecError> errors = spec1.test(o);
            if (!errors.isEmpty()) System.out.println(errors);
            return errors.isEmpty();
        }));


    }

    private boolean dependencies(JsObj o) {
        if (o.containsKey("a") && !o.containsKey("c")) return false;
        if (o.containsKey("b") && !o.containsKey("d")) return false;
        return true;
    }

    @Test
    public void testMapOfLong() {

        testOfMap(LONG_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsLong.of(1),
                                    "2",
                                    JsLong.of(2)
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfLong()
                              )
                 );
    }

    @Test
    public void testMapOfDecimal() {

        testOfMap(DECIMAL_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsDouble.of(1.5),
                                    "2",
                                    JsBigDec.of(BigDecimal.TEN)
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfDecimal()
                              )
                 );
    }

    @Test
    public void testMapOfStr() {

        testOfMap(STRING_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsStr.of("1"),
                                    "2",
                                    JsStr.of("2")
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfStr()
                              )
                 );

    }

    @Test
    public void testMapOfArray() {

        testOfMap(ARRAY_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsArray.of("1"),
                                    "2",
                                    JsArray.of("2")
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfArray()
                              )
                 );

    }

    @Test
    public void testMapOfBool() {

        testOfMap(BOOLEAN_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    TRUE,
                                    "2",
                                    JsBool.FALSE
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfBool()
                              )
                 );

    }

    @Test
    public void testMapOfInteger() {

        testOfMap(INT_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsInt.of(1),
                                    "2",
                                    JsInt.of(2)
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfInteger()
                              )
                 );

    }

    @Test
    public void testMapOfBigInteger() {

        testOfMap(INTEGRAL_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsInt.of(1),
                                    "2",
                                    JsBigInt.of(BigInteger.TEN)
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfBigInteger()
                              )
                 );

    }

    @Test
    public void testMapOfObj() {

        testOfMap(OBJ_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsObj.empty(),
                                    "2",
                                    JsObj.empty()
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfObj()
                              )
                 );

    }


    @Test
    public void testMapOfInstant() {

        testOfMap(INSTANT_EXPECTED,
                  JsObj.of("a",
                           JsObj.of("1",
                                    JsInstant.of(Instant.MIN),
                                    "2",
                                    JsInstant.of(Instant.MAX)
                                   )
                          ),
                  JsObjSpec.of("a",
                               mapOfInstant()
                              )
                 );


    }

    private void testOfMap(ERROR_CODE expectedCode,
                           JsObj valid,
                           JsObjSpec spec
                          ) {

        JsObj invalid = JsObj.of("a",
                                 JsObj.of("1",
                                          JsNull.NULL,
                                          "2",
                                          JsNull.NULL
                                         )
                                );

        Assertions.assertTrue(spec.test(valid).isEmpty());

        Set<SpecError> errors = spec.test(invalid);

        Assertions.assertFalse(errors.isEmpty());

        JsObjSpecParser parser = new JsObjSpecParser(spec);

        Assertions.assertEquals(valid,
                                parser.parse(valid.toPrettyString())
                               );

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(invalid.toPrettyString())
                               );

        Assertions.assertTrue(errors.stream().allMatch(it -> it.errorCode == expectedCode));
    }
}
