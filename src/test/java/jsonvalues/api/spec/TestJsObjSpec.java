package jsonvalues.api.spec;


import static jsonvalues.JsBool.TRUE;
import static jsonvalues.api.spec.FunTest.assertErrorIs;
import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.CONSTANT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.DECIMAL_CONDITION;
import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.INSTANT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.INT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.LONG_CONDITION;
import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.NULL_NOT_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.REQUIRED;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;
import static jsonvalues.spec.ERROR_CODE.STRING_CONDITION;
import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;
import static jsonvalues.spec.JsSpecs.any;
import static jsonvalues.spec.JsSpecs.array;
import static jsonvalues.spec.JsSpecs.arrayOfBigInt;
import static jsonvalues.spec.JsSpecs.arrayOfBool;
import static jsonvalues.spec.JsSpecs.arrayOfDec;
import static jsonvalues.spec.JsSpecs.arrayOfDecSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfDouble;
import static jsonvalues.spec.JsSpecs.arrayOfInt;
import static jsonvalues.spec.JsSpecs.arrayOfIntSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfLong;
import static jsonvalues.spec.JsSpecs.arrayOfLongSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfObj;
import static jsonvalues.spec.JsSpecs.arrayOfSpec;
import static jsonvalues.spec.JsSpecs.arrayOfStr;
import static jsonvalues.spec.JsSpecs.arrayOfStrSuchThat;
import static jsonvalues.spec.JsSpecs.arraySuchThat;
import static jsonvalues.spec.JsSpecs.bigInteger;
import static jsonvalues.spec.JsSpecs.binary;
import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.cons;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.doubleNumber;
import static jsonvalues.spec.JsSpecs.instant;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.longInteger;
import static jsonvalues.spec.JsSpecs.mapOfBigInteger;
import static jsonvalues.spec.JsSpecs.mapOfBool;
import static jsonvalues.spec.JsSpecs.mapOfDecimal;
import static jsonvalues.spec.JsSpecs.mapOfDouble;
import static jsonvalues.spec.JsSpecs.mapOfInstant;
import static jsonvalues.spec.JsSpecs.mapOfInteger;
import static jsonvalues.spec.JsSpecs.mapOfLong;
import static jsonvalues.spec.JsSpecs.mapOfObj;
import static jsonvalues.spec.JsSpecs.mapOfStr;
import static jsonvalues.spec.JsSpecs.obj;
import static jsonvalues.spec.JsSpecs.str;

import fun.gen.Combinators;
import fun.gen.Gen;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBinary;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsDoubleGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.ArraySchema;
import jsonvalues.spec.ERROR_CODE;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsParserException;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import jsonvalues.spec.SpecError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsObjSpec {


  @Test
  public void testIsStrSpec() {

    final JsObjSpec spec = JsObjSpec.of("a",
                                        str()
                                       );

    final List<SpecError> errors = spec.test(JsObj.of("a",
                                                      JsInt.of(1)
                                                     ));

    Assertions.assertFalse(errors.isEmpty());

    final SpecError pair = errors.stream()
                                 .findFirst()
                                 .get();

    Assertions.assertEquals(pair.error.code(),
                            STRING_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value(),
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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

    final List<SpecError> errors = spec.test(JsObj.of("a",
                                                      JsInt.of(5),
                                                      "b",
                                                      JsInstant.of(Instant.now())
                                                     ));

    Assertions.assertEquals(2,
                            errors.size()
                           );

    final SpecError intError = errors.stream()
                                     .filter(it -> it.error.value()
                                                           .equals(JsInt.of(5)))
                                     .findFirst().
                                     get();

    Assertions.assertEquals(intError.error.code(),
                            INT_CONDITION
                           );

    Assertions.assertEquals(intError.error.value(),
                            JsInt.of(5)
                           );

    Assertions.assertEquals(intError.path,
                            JsPath.fromKey("a")
                           );

    final SpecError instantError = errors.stream()
                                         .filter(it -> it.path.last()
                                                              .isKey(a -> a.equals("b")))
                                         .findFirst().
                                         get();

    Assertions.assertEquals(instantError.error.code(),
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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

    final List<SpecError> error = spec.test(JsObj.of("a",
                                                     JsLong.of(4)
                                                    ));

    Assertions.assertFalse(error.isEmpty());

    final SpecError pair = error.stream()
                                .findFirst()
                                .get();

    Assertions.assertEquals(pair.error.code(),
                            LONG_CONDITION
                           );

    Assertions.assertEquals(pair.error.value(),
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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
    final List<SpecError> error = spec.test(JsObj.of("a",
                                                     bd
                                                    ));

    Assertions.assertFalse(error.isEmpty());

    final SpecError pair = error.stream()
                                .findFirst()
                                .get();

    Assertions.assertEquals(pair.error.code(),
                            DECIMAL_CONDITION
                           );

    Assertions.assertEquals(pair.error.value(),
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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
  public void testIsNumberSpec() {

    final JsObjSpec spec = JsObjSpec.of("a",
                                        bigInteger(),
                                        "b",
                                        bigInteger(),
                                        "c",
                                        bigInteger(),
                                        "d",
                                        decimal()
                                       );

    final List<SpecError> error = spec.test(JsObj.of("a",
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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
                                        JsSpecs.oneValOf(List.of(TRUE)),
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
                                        JsSpecs.oneStringOf(Arrays.asList("A",
                                                                          "B"
                                                                         )),
                                        "h",
                                        JsSpecs.binary(it -> it.length <= 100)
                                               .nullable()
                                       );

    final List<SpecError> error = spec.test(JsObj.of("a",
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

    final List<SpecError> error = spec.test(JsObj.of("a",
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
                                        arrayOfDouble(b -> b > 1.5d),
                                        "e",
                                        arrayOfBigInt(i -> i.longValue() < 100),
                                        "f",
                                        JsObjSpec.of("a",
                                                     arrayOfNumber(),
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
                                                     doubleNumber(),
                                                     "f",
                                                     obj(JsObj::isEmpty)
                                                    )
                                                 .withOptKeys("b")
                                       )
                                    .withOptKeys("c",
                                                 "c"
                                                );

    final List<SpecError> error = spec.test(JsObj.of("a",
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
    final List<SpecError> error = spec.test(obj);

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
                                 )
                              .withOptKeys("b",
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
                                              )
                                           .withOptKeys("b")
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
                                  arrayOfSpec(JsObjSpec.of("a",
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
                                                           decimal()
                                                          )
                                                       .lenient()
                                             )
                                 )
                              .withOptKeys("h");

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
                                  arrayOfSpec(JsObjSpec.of("a",
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
                                                           decimal().nullable()
                                                          )
                                                       .lenient()
                                             ).nullable()
                                 );

    final List<SpecError> result = spec.test(JsObj.of("a",
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

  private JsSpec arrayOfNumber() {
    return arrayOfSpec(JsSpecs.oneSpecOf(JsSpecs.integer(),
                                         JsSpecs.longInteger(),
                                         JsSpecs.bigInteger(),
                                         JsSpecs.doubleNumber(),
                                         JsSpecs.decimal())
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
                                  arrayOfSpec(JsObjSpec.of("a",
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
                                                           decimal().nullable()
                                                          )
                                                       .lenient()
                                                       .withOptKeys("a",
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
                                 )
                              .withOptKeys("a",
                                           "c",
                                           "d",
                                           "e",
                                           "f",
                                           "g",
                                           "h",
                                           "i",
                                           "j",
                                           "k"
                                          )
                              .lenient();

    final List<SpecError> result = spec.test(JsObj.of("a",
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

    final List<SpecError> result1 = spec.test(JsObj.of("a",
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
                                  arrayOfSpec(JsObjSpec.of("a",
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
                                                           decimal()
                                                          )
                                                       .lenient()
                                                       .withOptKeys("a",
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
                                 )
                              .withOptKeys("a",
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
                                          )
                                       .lenient();
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
                                 )
                              .withOptKeys("b",
                                           "c",
                                           "d"
                                          );

    final List<SpecError> set = spec.test(JsObj.of("a",
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

    final JsSpec a = arrayOfSpec(JsObjSpec.of("a",
                                              bigInteger()
                                             )
                                          .lenient());
    final JsSpec b = arrayOfSpec(JsObjSpec.of("a",
                                              str()
                                             )
                                          .lenient()).nullable();
    final JsSpec c = arrayOfSpec(JsObjSpec.of("a",
                                              bigInteger()
                                             )
                                          .lenient()).nullable();
    final JsSpec d = arrayOfSpec(JsObjSpec.of("a",
                                              bigInteger()
                                             )
                                          .lenient());

    final JsObjSpec objspec = JsObjSpec.of("a",
                                           a,
                                           "b",
                                           b,
                                           "c",
                                           c,
                                           "d",
                                           d
                                          )
                                       .withOptKeys("c",
                                                    "d"
                                                   );

    final List<SpecError> errors = objspec.test(JsObj.of("a",
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
                               arrayOfSpec(spec),
                               "b",
                               arrayOfSpec(spec).nullable(),
                               "c",
                               arrayOfSpec(spec),
                               "d",
                               arrayOfSpec(spec).nullable()

                              )
                           .withOptKeys("c",
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
                                       )
                                    .withOptKeys("a");

    final List<SpecError> errors = spec.test(JsObj.of("b",
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
                                        arrayOfSpec(JsObjSpec.of("a",
                                                                 str()
                                                                )
                                                             .lenient()
                                                   )
                                       );

    final List<SpecError> errors = spec.test(JsObj.of("a",
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

    List<SpecError> result = spec.test(JsObj.of("a",
                                                JsStr.of("ñññ"),
                                                "b",
                                                JsInt.of(1)
                                               ));
    Assertions.assertEquals(2,
                            result.size()
                           );

    Assertions.assertTrue(result.stream()
                                .anyMatch(e -> e.path.equals(JsPath.fromKey("b")) && e.error.code()
                                                                                            .equals(BINARY_EXPECTED)));

    Assertions.assertTrue(result.stream()
                                .anyMatch(e -> e.path.equals(JsPath.fromKey("a")) && e.error.code()
                                                                                            .equals(BINARY_EXPECTED)));


  }

  @Test
  public void testInstantSpec() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  instant(),
                                  "b",
                                  instant()
                                 );

    List<SpecError> errorPairs = spec.test(JsObj.of("a",
                                                    JsInstant.of(Instant.now()),
                                                    "b",
                                                    JsStr.of(Instant.now()
                                                                    .toString())
                                                   ));
    Assertions.assertTrue(errorPairs
                              .isEmpty());

    List<SpecError> errorPairs1 = spec.test(JsObj.of("a",
                                                     JsStr.of("hola"),
                                                     "b",
                                                     JsStr.of(LocalDateTime.now(ZoneId.systemDefault())
                                                                           .toString()
                                                             )
                                                    ));

    Assertions.assertEquals(2,
                            errorPairs1.size()
                           );

    Assertions.assertTrue(errorPairs1.stream()
                                     .anyMatch(e -> e.path.equals(JsPath.fromKey("b")) && e.error.code()
                                                                                                 .equals(INSTANT_EXPECTED)));

    Assertions.assertTrue(errorPairs1.stream()
                                     .anyMatch(e -> e.path.equals(JsPath.fromKey("a")) && e.error.code()
                                                                                                 .equals(INSTANT_EXPECTED)));


  }

  @Test
  public void testConstantSpec() {

    List<SpecError> errorPairSet = cons(JsStr.of("hi")).test(JsPath.empty(),
                                                             JsInt.of(1)
                                                            );

    Assertions.assertTrue(errorPairSet.stream()
                                      .map(it -> it.error.code())
                                      .toList()
                                      .contains(CONSTANT_CONDITION));

    Assertions.assertTrue(errorPairSet.stream()
                                      .map(it -> it.error.value())
                                      .toList()
                                      .contains(JsInt.of(1)));

    JsObjSpec spec = JsObjSpec.of("a",
                                  cons(JsInt.of(2))
                                 )
                              .withOptKeys("a");

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

    List<SpecError> errors = spec.test(JsObj.of("a",
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

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

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

    Assertions.assertTrue(gen.sample(10000)
                             .allMatch(o -> {
                               List<SpecError> errors = spec.test(o);
                               return errors.isEmpty();
                             }));

    Assertions.assertTrue(baseGen.suchThat(spec)
                                 .sample(10000)
                                 .allMatch(o -> {
                                   List<SpecError> errors = spec.test(o);
                                   return errors.isEmpty();
                                 }));

    JsObjSpec spec1 = baseSpec.withAllOptKeys()
                              .suchThat(o -> dependencies(o));
    Assertions.assertTrue(baseGen.withAllOptKeys()
                                 .suchThat(spec1)
                                 .sample(10000)
                                 .allMatch(o -> {
                                   List<SpecError> errors = spec1.test(o);
                                   return errors.isEmpty();
                                 }));


  }

  private boolean dependencies(JsObj o) {
    if (o.containsKey("a") && !o.containsKey("c")) {
      return false;
    }
    if (o.containsKey("b") && !o.containsKey("d")) {
      return false;
    }
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
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
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
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
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
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
                               )
                      ),
              JsObjSpec.of("a",
                           mapOfStr()
                          )
             );

  }

  @Test
  public void testMapOfArray() {

    testOfMap(NULL_NOT_EXPECTED,
              JsObj.of("a",
                       JsObj.of("1",
                                JsArray.of(1),
                                "2",
                                JsArray.of(2)
                               )
                      ),
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
                               )
                      ),
              JsObjSpec.of("a",
                           JsSpecs.mapOfSpec(JsSpecs.arrayOfInt())
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
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
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
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
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
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
                               )
                      ),
              JsObjSpec.of("a",
                           mapOfBigInteger()
                          )
             );

  }

  @Test
  public void testMapOfObj() {

    testOfMap(INT_EXPECTED,
              JsObj.of("ages",
                       JsObj.of("Rafa",
                                JsObj.of("age",
                                         JsInt.of(1)),
                                "Pedro",
                                JsObj.of("age",
                                         JsInt.of(1))
                               )
                      ),
              JsObj.of("ages",
                       JsObj.of("1",
                                JsObj.of("age",
                                         JsStr.of("1")),
                                "2",
                                JsObj.of("age",
                                         JsStr.of("2"))
                               )
                      ),
              JsObjSpec.of("ages",
                           mapOfObj(JsObjSpec.of("age",
                                                 JsSpecs.integer()))
                          )
             );

  }


  @Test
  public void testMapOfDouble() {

    testOfMap(DOUBLE_EXPECTED,
              JsObj.of("ages",
                       JsObj.of("Rafa",
                                JsDouble.of(1 / 2d),
                                "Pedro",
                                JsDouble.of(1 / 4d)
                               )
                      ),
              JsObj.of("ages",
                       JsObj.of("1",
                                JsStr.of("1"),
                                "2",
                                JsStr.of("1")
                               )
                      ),
              JsObjSpec.of("ages",
                           mapOfDouble()
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
              JsObj.of("a",
                       JsObj.of("1",
                                JsNull.NULL,
                                "2",
                                JsNull.NULL
                               )
                      ),
              JsObjSpec.of("a",
                           mapOfInstant()
                          )
             );


  }

  private void testOfMap(ERROR_CODE expectedCode,
                         JsObj valid,
                         JsObj invalid,
                         JsObjSpec spec
                        ) {

    Assertions.assertTrue(spec.test(valid)
                              .isEmpty());

    List<SpecError> errors = spec.test(invalid);

    Assertions.assertFalse(errors.isEmpty());

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertEquals(valid,
                            parser.parse(valid.toPrettyString())
                           );

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(invalid.toPrettyString())
                           );

    Assertions.assertTrue(errors.stream()
                                .allMatch(it -> it.error.code() == expectedCode));
  }

  @Test
  public void testDoubleSpec() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  arrayOfDouble(s -> s > 1.5,
                                                ArraySchema.sizeBetween(1,
                                                                        10)).nullable(),
                                  "b",
                                  doubleNumber(s -> s > 0.0d).nullable()
                                 );
    JsObjGen gen = JsObjGen.of("a",
                               Combinators.oneOf(Gen.cons(JsNull.NULL),
                                                 JsArrayGen.ofN(JsDoubleGen.arbitrary(1.5d,
                                                                                      10d),
                                                                10)),
                               "b",
                               Combinators.oneOf(Gen.cons(JsNull.NULL),
                                                 JsDoubleGen.arbitrary(0.0d,
                                                                       1.0d))
                              );

    gen.sample(100)
       .forEach(obj -> Assertions.assertTrue(spec.test(obj)
                                                 .isEmpty())
               );

  }

  @Test
  public void testReqKeys() {

    JsObjSpec spec = JsObjSpec.of(
                                  "a",
                                  str(),
                                  "b",
                                  integer()
                                 )
                              .withReqKeys("a",
                                           "b");

    List<SpecError> errors = spec.test(JsObj.empty())
                                 .stream()
                                 .toList();
    Assertions.assertEquals(2,
                            errors.size());
    for (SpecError error : errors) {
      Assertions.assertSame(error.error.code(),
                            REQUIRED);
    }

  }

  @Test
  public void testOptKeys() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  str(),
                                  "b",
                                  integer()
                                 )
                              .withOptKeys(List.of("a",
                                                   "b"));

    List<SpecError> errors = spec.test(JsObj.empty())
                                 .stream()
                                 .toList();
    Assertions.assertEquals(0,
                            errors.size());
  }
}
