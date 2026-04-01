package jsonvalues.api.spec;

import static jsonvalues.api.spec.FunTest.assertErrorIs;
import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;
import static jsonvalues.spec.JsSpecs.any;
import static jsonvalues.spec.JsSpecs.arrayOfBigIntSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfDec;
import static jsonvalues.spec.JsSpecs.arrayOfDouble;
import static jsonvalues.spec.JsSpecs.arrayOfInt;
import static jsonvalues.spec.JsSpecs.arrayOfLong;
import static jsonvalues.spec.JsSpecs.arrayOfObj;
import static jsonvalues.spec.JsSpecs.arrayOfObjSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfSpec;
import static jsonvalues.spec.JsSpecs.arrayOfStr;
import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.str;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsStr;
import jsonvalues.spec.BigIntSchema;
import jsonvalues.spec.DecimalSchema;
import jsonvalues.spec.DoubleSchema;
import jsonvalues.spec.IntegerSchema;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsParserException;
import jsonvalues.spec.JsSpecs;
import jsonvalues.spec.LongSchema;
import jsonvalues.spec.SpecError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsArraySpecTest {

  @Test
  void shouldReturnSpecMissingWhenTupleReceivesExtraElement() {

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
  void shouldValidateTupleArityForAnySpecs() {

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
  void shouldValidateArrayOfBooleanWithPredicate() {
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
  void shouldAcceptOnlyTwoElementsForTwoSlotTuple() {

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
  void shouldValidateIntegralArrayElementTypes() {
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
  void shouldValidateNumericArrayElementTypes() {
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
  void shouldValidateArrayOfObjectsWithArrayPredicate() {
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
  void shouldValidateComplexObjectWithNestedArraySpecs() {

    String json_str =
        "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 21, \"latitude\": 48.858093, \"longitude\": 2.294694, \"fruits\": [ \"apple\", \"orange\", \"pear\" ], \"numbers\": [ 1, 2, 3, 4, 5, 6,"
        + " 7, 8, 9, 10 ], \"vegetables\": [ { \"veggieName\": \"potato\", " +
        " \"veggieLike\": true }, { \"veggieName\": \"broccoli\", \"veggieLike\": false } ]} "
        + "\"veggieName\": \"broccoli\", \"veggieLike\": false } ]}";

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
  void shouldReportTypeErrorsForArrayElementSpecs() {

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
  void shouldAcceptNullableArrayOfSpecInObjectFields() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  str(),
                                  "b",
                                  integer()
                                 )
                              .lenient();
    JsObjSpec objSpec = JsObjSpec.of("a",
                                     arrayOfSpec(spec).nullable(),
                                     "b",
                                     arrayOfSpec(spec).nullable()
                                    )
                                 .withOptKeys("b");

    Assertions.assertTrue(objSpec.test(JsObj.of("a",
                                                JsNull.NULL
                                               ))
                                 .isEmpty()
                         );


  }

  @Test
  void shouldParseArrayOfDoubleWhenValuesAreValid() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  arrayOfDouble());

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObj obj = JsObj.of("a",
                         JsArray.of(1 / 2d,
                                    1 / 4d));

    Assertions.assertTrue(spec.test(obj)
                              .isEmpty());

    JsObj obj2 = parser.parse(obj.toString());

    Assertions.assertEquals(obj,
                            obj2);
  }

  @Test
  void shouldApplyNumericArrayElementSchemasInTestAndParser() {
    var intSpec = JsSpecs.arrayOfInt(IntegerSchema.between(0,
                                                           1));
    var longSpec = JsSpecs.arrayOfLong(LongSchema.between(0,
                                                          1));
    var doubleSpec = JsSpecs.arrayOfDouble(DoubleSchema.between(0.0,
                                                                1.0));
    var decimalSpec = JsSpecs.arrayOfDec(DecimalSchema.between(BigDecimal.ZERO,
                                                               BigDecimal.ONE));
    var bigIntSpec = JsSpecs.arrayOfBigInt(BigIntSchema.between(BigInteger.ZERO,
                                                                BigInteger.ONE));

    Assertions.assertFalse(intSpec.test(JsArray.of(2))
                                  .isEmpty());
    Assertions.assertFalse(longSpec.test(JsArray.of(2))
                                   .isEmpty());
    Assertions.assertFalse(doubleSpec.test(JsArray.of(2.0))
                                     .isEmpty());
    Assertions.assertFalse(decimalSpec.test(JsArray.of(2))
                                      .isEmpty());
    Assertions.assertFalse(bigIntSpec.test(JsArray.of(2))
                                     .isEmpty());

    Assertions.assertThrows(JsParserException.class,
                            () -> intSpec.parse("[2]"));
    Assertions.assertThrows(JsParserException.class,
                            () -> longSpec.parse("[2]"));
    Assertions.assertThrows(JsParserException.class,
                            () -> doubleSpec.parse("[2.0]"));
    Assertions.assertThrows(JsParserException.class,
                            () -> decimalSpec.parse("[2]"));
    Assertions.assertThrows(JsParserException.class,
                            () -> bigIntSpec.parse("[2]"));
  }

  @Test
  void shouldKeepParentPathForTupleElementErrors() {
    var spec = JsObjSpec.of("a",
                            JsSpecs.tuple(JsSpecs.integer()));

    List<SpecError> errors = spec.test(JsObj.of("a",
                                                JsArray.of("x")));

    assertErrorIs(errors,
                  INT_EXPECTED,
                  JsStr.of("x"),
                  JsPath.fromKey("a")
                        .index(0));
  }
}
