package jsonvalues.api.spec;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.spec.JsSpecs.any;
import static jsonvalues.spec.JsSpecs.array;
import static jsonvalues.spec.JsSpecs.arrayOfBigInt;
import static jsonvalues.spec.JsSpecs.arrayOfBool;
import static jsonvalues.spec.JsSpecs.arrayOfDec;
import static jsonvalues.spec.JsSpecs.arrayOfInt;
import static jsonvalues.spec.JsSpecs.arrayOfLong;
import static jsonvalues.spec.JsSpecs.arrayOfObj;
import static jsonvalues.spec.JsSpecs.arrayOfStr;
import static jsonvalues.spec.JsSpecs.arraySuchThat;
import static jsonvalues.spec.JsSpecs.bigInteger;
import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.doubleNumber;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.longInteger;
import static jsonvalues.spec.JsSpecs.obj;
import static jsonvalues.spec.JsSpecs.str;
import static jsonvalues.spec.JsSpecs.tuple;

import fun.gen.BytesGen;
import fun.gen.Gen;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsBigDecGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.JsArraySpecParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsParserException;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsObjParser {

  @Test
  public void test_parse_obj_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  obj(a -> a.containsKey("a"))
                                 )
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsObj.of("b",
                                                                 JsBool.TRUE
                                                                )
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_int_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  integer(i -> i > 0),
                                  "b",
                                  longInteger(i -> i > 0)
                                 )
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsInt.of(3),
                                                        "b",
                                                        JsLong.of(-1)
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_long_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  longInteger(i -> i > 0)
                                 )
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsLong.of(-1L)
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_integral_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  bigInteger(i -> i.longValueExact() > 0)
                                 )
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsLong.of(-1L)
                                                       )
                                                    .toPrettyString())
                           );

  }


  @Test
  public void test_parse_number_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  decimal()
                                 )
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsBool.TRUE
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_string_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  str(i -> i.length() == 3)
                                 )
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsStr.of("ab")
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_decimal_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  decimal(i -> i.divide(BigDecimal.TEN)
                                                .compareTo(new BigDecimal(1)) == 0)
                                 )
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsBigDec.of(BigDecimal.ONE)
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_obj_all_primitive_types() {

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
                                        JsSpecs.oneValOf(List.of(FALSE)),
                                        "g",
                                        decimal(),
                                        "h",
                                        bigInteger(),
                                        "i",
                                        JsObjSpec.of("a",
                                                     doubleNumber(),
                                                     "b",
                                                     array(),
                                                     "c",
                                                     obj(),
                                                     "d",
                                                     integer(i -> i % 2 == 0),
                                                     "e",
                                                     longInteger(i -> i % 2 == 1),
                                                     "f",
                                                     str(s -> s.startsWith("a")),
                                                     "g",
                                                     decimal(d -> d.doubleValue() > 1.0),
                                                     "h",
                                                     obj(o -> o.containsKey("b")),
                                                     "i",
                                                     arraySuchThat(a -> a.head()
                                                                         .equals(JsStr.of("first"))).nullable(),
                                                     "j",
                                                     tuple(decimal(),
                                                           any()
                                                          )
                                                    )
                                       )
                                    .withOptKeys("f",
                                                 "i"
                                                );

    final JsObj obj = JsObj.of("a",
                               JsInt.of(1),
                               "b",
                               JsStr.of("a"),
                               "c",
                               JsLong.of(100),
                               "d",
                               JsBool.FALSE,
                               "e",
                               JsBool.TRUE,
                               "f",
                               JsBool.FALSE,
                               "g",
                               JsBigDec.of(BigDecimal.TEN),
                               "h",
                               JsBigInt.of(BigInteger.TEN),
                               "i",
                               JsObj.of("a",
                                        JsDouble.of(1.2),
                                        "b",
                                        JsArray.empty(),
                                        "c",
                                        JsObj.empty(),
                                        "d",
                                        JsInt.of(10),
                                        "e",
                                        JsLong.of(13),
                                        "f",
                                        JsStr.of("a"),
                                        "g",
                                        JsBigDec.of(new BigDecimal("3.0")),
                                        "h",
                                        JsObj.of("b",
                                                 JsStr.of("b")
                                                ),
                                        "i",
                                        JsArray.of(JsStr.of("first"),
                                                   JsBool.TRUE
                                                  ),
                                        "j",
                                        JsArray.of(JsDouble.of(1.2),
                                                   JsBool.TRUE
                                                  )
                                       )
                              );

    Assertions.assertEquals(obj,
                            JsObjSpecParser.of(spec)
                                           .parse(obj.toPrettyString())
                           );

  }


  @Test
  public void test_parse_obj_all_array_of_primitive_types() {

    final JsObjSpec spec = JsObjSpec.of("a",
                                        arrayOfInt(),
                                        "b",
                                        arrayOfStr(),
                                        "c",
                                        arrayOfBool(),
                                        "d",
                                        arrayOfDec(),
                                        "e",
                                        arrayOfDec(),
                                        "f",
                                        arrayOfBigInt(),
                                        "g",
                                        arrayOfLong(),
                                        "h",
                                        JsObjSpec.of("a",
                                                     arrayOfInt(v -> v > 5),
                                                     "b",
                                                     arrayOfStr(s -> s.startsWith("a")),
                                                     "c",
                                                     arrayOfLong(l -> l < 10),
                                                     "d",
                                                     arrayOfLong(i -> i > 0).nullable(),
                                                     "e",
                                                     arrayOfLong(i -> i > 0).nullable(),
                                                     "f",
                                                     arrayOfLong(i -> i > 0).nullable(),
                                                     "g",
                                                     arrayOfLong(i -> i > 0).nullable()
                                                    )
                                                 .withOptKeys("g")
                                       );

    final JsObj obj = JsObj.of("a",
                               JsArray.of(1,
                                          2,
                                          3
                                         ),
                               "b",
                               JsArray.of("a",
                                          "b",
                                          "c"
                                         ),
                               "c",
                               JsArray.of(true,
                                          false,
                                          true
                                         ),
                               "d",
                               JsArray.of(1.2,
                                          1.3,
                                          1.5
                                         ),
                               "e",
                               JsArray.of(JsInt.of(1),
                                          JsLong.of(10),
                                          JsDouble.of(1.5)
                                         ),
                               "f",
                               JsArray.of(1,
                                          10,
                                          1000000
                                         ),
                               "g",
                               JsArray.of(1,
                                          2,
                                          4
                                         ),
                               "h",
                               JsObj.of("a",
                                        JsArray.of(13,
                                                   14,
                                                   15
                                                  ),
                                        "b",
                                        JsArray.of("ab",
                                                   "ac"
                                                  ),
                                        "c",
                                        JsArray.of(1,
                                                   2,
                                                   3
                                                  ),
                                        "d",
                                        JsArray.of(1,
                                                   2
                                                  ),
                                        "e",
                                        JsNull.NULL,
                                        "f",
                                        JsArray.of(4)

                                       )
                              );

    Assertions.assertEquals(obj,
                            JsObjSpecParser.of(spec)
                                           .parse(obj.toPrettyString())
                           );

  }


  @Test
  public void test_parse_obj_all_primitive_types_with_predicates() {

    final JsObjSpec spec =
        JsObjSpec.of("a",
                     integer(i -> i > 0),
                     "b",
                     str(a -> !a.isEmpty()),
                     "c",
                     longInteger(c -> c > 0),
                     "d",
                     bool(),
                     "e",
                     JsSpecs.oneValOf(List.of(TRUE)),
                     "f",
                     JsSpecs.oneValOf(List.of(FALSE)),
                     "g",
                     decimal(d -> d.doubleValue() > 0.0),
                     "h",
                     bigInteger(i -> i.longValueExact() % 2 == 0),
                     "i",
                     JsObjSpec.of("a",
                                  decimal(),
                                  "b",
                                  array(),
                                  "c",
                                  obj(JsObj::isEmpty),
                                  "d",
                                  integer(i -> i % 2 == 0),
                                  "e",
                                  longInteger(i -> i % 2 == 1),
                                  "f",
                                  str(s -> s.startsWith("a")),
                                  "g",
                                  decimal(d -> d.doubleValue() > 1.0),
                                  "h",
                                  obj(o -> o.containsKey("b")),
                                  "i",
                                  arraySuchThat(a -> a.head()
                                                      .equals(JsStr.of("first"))),
                                  "j",
                                  tuple(decimal(),
                                        any()
                                       )
                                 )
                    )
                 .withOptKeys("e");

    final JsObj obj = JsObj.of("a",
                               JsInt.of(1),
                               "b",
                               JsStr.of("a"),
                               "c",
                               JsLong.of(100),
                               "d",
                               JsBool.FALSE,
                               "e",
                               JsBool.TRUE,
                               "f",
                               JsBool.FALSE,
                               "g",
                               JsBigDec.of(BigDecimal.TEN),
                               "h",
                               JsBigInt.of(BigInteger.TEN),
                               "i",
                               JsObj.of("a",
                                        JsDouble.of(1.2),
                                        "b",
                                        JsArray.empty(),
                                        "c",
                                        JsObj.empty(),
                                        "d",
                                        JsInt.of(10),
                                        "e",
                                        JsLong.of(13),
                                        "f",
                                        JsStr.of("a"),
                                        "g",
                                        JsBigDec.of(new BigDecimal("3.0")),
                                        "h",
                                        JsObj.of("b",
                                                 JsStr.of("b")
                                                ),
                                        "i",
                                        JsArray.of(JsStr.of("first"),
                                                   JsBool.TRUE
                                                  ),
                                        "j",
                                        JsArray.of(JsDouble.of(1.2),
                                                   JsBool.TRUE
                                                  )
                                       )
                              );

    Assertions.assertEquals(obj,
                            JsObjSpecParser.of(spec)
                                           .parse(obj.toPrettyString())
                           );

  }


  @Test
  public void test_required_fields() {
    final JsObjSpec spec = JsObjSpec.of("a",
                                        integer().nullable(),
                                        "b",
                                        str().nullable(),
                                        "c",
                                        longInteger().nullable(),
                                        "d",
                                        obj().nullable(),
                                        "e",
                                        array().nullable(),
                                        "f",
                                        bool().nullable(),
                                        "g",
                                        decimal().nullable(),
                                        "h",
                                        bigInteger()
                                            .nullable(),
                                        "i",
                                        tuple(arrayOfInt().nullable(),
                                              arrayOfLong().nullable()

                                             )
                                        ,
                                        "j",
                                        obj()
                                            .nullable(),
                                        "k",
                                        obj(a -> a.keySet()
                                                  .size() == 2)
                                            .nullable(),
                                        "j",
                                        arrayOfObj(a -> a.keySet()
                                                         .size() == 2).nullable()
                                       )
                                    .withOptKeys("a",
                                                 "b",
                                                 "c",
                                                 "d",
                                                 "e",
                                                 "f",
                                                 "g",
                                                 "h",
                                                 "i",
                                                 "j",
                                                 "k",
                                                 "j"
                                                );

    Assertions.assertEquals(JsObj.empty(),
                            JsObjSpecParser.of(spec)
                                           .parse(JsObj.empty()
                                                       .toPrettyString())
                           );

    final JsObj obj = JsObj.of("a",
                               JsNull.NULL,
                               "b",
                               JsNull.NULL,
                               "c",
                               JsNull.NULL,
                               "d",
                               JsNull.NULL,
                               "e",
                               JsNull.NULL,
                               "g",
                               JsNull.NULL,
                               "h",
                               JsNull.NULL,
                               "i",
                               JsArray.of(JsNull.NULL,
                                          JsNull.NULL
                                         ),
                               "j",
                               JsObj.empty(),
                               "k",
                               JsObj.of("a",
                                        JsInt.of(1),
                                        "b",
                                        JsStr.of("a")
                                       ),
                               "j",
                               JsArray.of(JsObj.of("a",
                                                   JsBool.TRUE,
                                                   "b",
                                                   JsBool.FALSE
                                                  ))
                              );

    Assertions.assertEquals(obj,
                            JsObjSpecParser.of(spec)
                                           .parse(obj.toPrettyString())
                           );
  }

  @Test
  public void test_strict_mode() {
    final JsObjSpec spec = JsObjSpec.of("a",
                                        str()
                                            .nullable()
                                       )
                                    .lenient()
                                    .withOptKeys("a");

    final JsObj obj = JsObj.of("b",
                               JsStr.of("b")
                              );

    final JsObj parsed = JsObjSpecParser.of(spec)
                                        .parse(obj.toPrettyString());

    Assertions.assertEquals(obj,
                            parsed
                           );
  }


  @Test
  public void test_int_spec() {

    JsObjSpec isint = JsObjSpec.of("a",
                                   integer(i -> i > 0).nullable(),
                                   "b",
                                   integer(i -> i > 0)
                                       .nullable(),
                                   "c",
                                   integer(i -> i > 0),
                                   "d",
                                   integer(i -> i < 0).nullable(),
                                   "e",
                                   integer(i -> i % 2 == 1)
                                       .nullable(),
                                   "f",
                                   integer(i -> i % 2 == 0).nullable()
                                  )
                               .withOptKeys("b",
                                            "e"
                                           );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsInt.of(3),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjSpecParser parser = JsObjSpecParser.of(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );

    final JsObj b = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsInt.of(2),
                             "c",
                             JsInt.of(3),
                             "d",
                             JsInt.of(-5),
                             "e",
                             JsInt.of(11),
                             "f",
                             JsInt.of(20)
                            );

    Assertions.assertEquals(b,
                            parser.parse(b.toString())
                           );
  }


  @Test
  public void test_obj_spec() {

    JsObjSpec isint = JsObjSpec.of("a",
                                   longInteger(i -> i > 0).nullable(),
                                   "b",
                                   longInteger(i -> i > 0).nullable(),
                                   "c",
                                   longInteger(i -> i > 0),
                                   "d",
                                   longInteger(i -> i < 0).nullable(),
                                   "e",
                                   longInteger(i -> i % 2 == 1).nullable(),
                                   "f",
                                   longInteger(i -> i % 2 == 0).nullable()
                                  )
                               .withOptKeys("b",
                                            "e"
                                           );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsLong.of(3L),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjSpecParser parser = JsObjSpecParser.of(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );

    final JsObj b = JsObj.of("a",
                             JsLong.of(1L),
                             "b",
                             JsLong.of(2L),
                             "c",
                             JsLong.of(3L),
                             "d",
                             JsLong.of(-5L),
                             "e",
                             JsLong.of(11L),
                             "f",
                             JsLong.of(20L)
                            );

    Assertions.assertEquals(b,
                            parser.parse(b.toString())
                           );
  }

  @Test
  public void test_dec_spec() {

    JsObjSpec isdec = JsObjSpec.of("a",
                                   decimal(i -> i.longValueExact() > 0).nullable(),
                                   "b",
                                   decimal(i -> i.longValueExact() > 0)
                                       .nullable(),
                                   "c",
                                   decimal(i -> i.longValueExact() > 0),
                                   "d",
                                   decimal(i -> i.longValueExact() < 0).nullable(),
                                   "e",
                                   decimal(i -> i.longValueExact() % 2 == 1)
                                       .nullable(),
                                   "f",
                                   decimal(i -> i.longValueExact() % 2 == 0).nullable()
                                  )
                               .withOptKeys("b",
                                            "e"
                                           );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsBigDec.of(new BigDecimal(3L)),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjSpecParser parser = JsObjSpecParser.of(isdec);
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );

    final JsObj b = JsObj.of("a",
                             JsBigDec.of(new BigDecimal(1L)),
                             "b",
                             JsBigDec.of(new BigDecimal(2L)),
                             "c",
                             JsBigDec.of(new BigDecimal(3L)),

                             "d",
                             JsBigDec.of(new BigDecimal(-5L)),

                             "e",
                             JsBigDec.of(new BigDecimal(1L)),
                             "f",
                             JsBigDec.of(new BigDecimal(20L))
                            );

    Assertions.assertEquals(b,
                            parser.parse(b.toString())
                           );
  }


  @Test
  public void test_integral_spec() {

    JsObjSpec isint = JsObjSpec.of("a",
                                   bigInteger(i -> i.longValueExact() > 0).nullable(),
                                   "b",
                                   bigInteger(i -> i.longValueExact() > 0).nullable(),
                                   "c",
                                   bigInteger(i -> i.longValueExact() > 0),
                                   "d",
                                   bigInteger(i -> i.longValueExact() < 0).nullable(),
                                   "e",
                                   bigInteger(i -> i.longValueExact() % 2 == 1).nullable(),
                                   "f",
                                   bigInteger(i -> i.longValueExact() % 2 == 0).nullable()
                                  )
                               .withOptKeys("b",
                                            "e"
                                           );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsInt.of(3),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjSpecParser parser = JsObjSpecParser.of(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );

    final JsObj b = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsInt.of(2),
                             "c",
                             JsInt.of(3),
                             "d",
                             JsInt.of(-5),
                             "e",
                             JsInt.of(11),
                             "f",
                             JsInt.of(20)
                            );

    Assertions.assertEquals(b,
                            parser.parse(b.toString())
                           );
  }


  @Test
  public void test_string_spec() {

    JsObjSpec isint = JsObjSpec.of("a",
                                   str(i -> i.length() > 3).nullable(),
                                   "b",
                                   str(i -> i.length() > 3).nullable(),
                                   "c",
                                   str(i -> i.length() > 3),
                                   "d",
                                   str(i -> i.length() > 2).nullable(),
                                   "e",
                                   str(i -> i.length() == 1).nullable(),
                                   "f",
                                   str(i -> i.length() % 2 == 0).nullable()
                                  )
                               .withOptKeys("b",
                                            "e"
                                           );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsStr.of("abcd"),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjSpecParser parser = JsObjSpecParser.of(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );

    final JsObj b = JsObj.of("a",
                             JsStr.of("abcd"),
                             "b",
                             JsStr.of("abcd"),
                             "c",
                             JsStr.of("abcd"),
                             "d",
                             JsStr.of("abcd"),
                             "e",
                             JsStr.of("a"),
                             "f",
                             JsStr.of("abcd")
                            );

    Assertions.assertEquals(b,
                            parser.parse(b.toString())
                           );
  }

  @Test
  public void testArrayOfObjSpec() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  tuple(str(),
                                        bool()
                                       )
                                      .nullable()
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    final JsObj a = JsObj.of("a",
                             JsArray.of(JsStr.of("a"),
                                        JsBool.TRUE
                                       )
                            );

    final JsObj b = JsObj.of("a",
                             JsNull.NULL
                            );
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );
    Assertions.assertEquals(b,
                            parser.parse(b.toString())
                           );
  }


  @Test
  public void test_value_parser() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  any(JsValue::isBool)
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsStr.of("a")
                                                       )
                                                    .toString()
                                              )
                           );


  }

  @Test
  public void test_array_value_parser() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  array(a -> a.isBool() || a.isIntegral())
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    final JsObj a = JsObj.of("a",
                             JsArray.of(JsBool.FALSE,
                                        JsInt.of(1)
                                       )
                            );
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );

  }


  @Test
  public void array_of_number_with_predicate() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  arrayOfBigInt(a -> a.longValueExact() > 0).nullable(),
                                  "b",
                                  arrayOfBigInt(a -> a.longValueExact() < 0).nullable(),
                                  "c",
                                  arrayOfBigInt(a -> a.longValueExact() % 2 == 0).nullable(),
                                  "e",
                                  arrayOfBigInt(a -> a.longValueExact() % 3 == 0).nullable()
                                 )
                              .withOptKeys("c",
                                           "e"
                                          );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    final JsObj valid = JsObj.of("a",
                                 JsArray.of(1,
                                            2
                                           ),
                                 "b",
                                 JsNull.NULL,
                                 "c",
                                 JsArray.of(2,
                                            4,
                                            6
                                           )
                                );
    Assertions.assertEquals(valid,
                            parser.parse(valid.toString())
                           );

    Assertions.assertThrows(JsParserException.class,
                            () ->
                                parser.parse(JsObj.of("a",
                                                      JsNull.NULL,
                                                      "b",
                                                      JsArray.of(1),
                                                      "c",
                                                      JsArray.of(2,
                                                                 4,
                                                                 6
                                                                )
                                                     )
                                                  .toPrettyString())
                           );

    Assertions.assertThrows(JsParserException.class,
                            () ->
                                parser.parse(JsObj.of("a",
                                                      JsNull.NULL,
                                                      "b",
                                                      JsArray.of(1,
                                                                 2
                                                                ),
                                                      "c",
                                                      JsArray.of(1,
                                                                 4,
                                                                 6
                                                                )
                                                     )
                                                  .toPrettyString())
                           );
  }

  @Test
  public void array_of_decimal_with_predicate() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  arrayOfDec(a -> a.longValueExact() > 0),
                                  "b",
                                  arrayOfDec(a -> a.longValueExact() < 0).nullable(),
                                  "c",
                                  arrayOfDec(a -> a.longValueExact() % 2 == 0),
                                  "e",
                                  arrayOfDec(a -> a.longValueExact() % 3 == 0).nullable()
                                 )
                              .withOptKeys("c",
                                           "e"
                                          );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    final JsObj a = JsObj.of("a",
                             JsArray.of(15,
                                        25
                                       ),
                             "b",
                             JsNull.NULL,
                             "c",
                             JsArray.of(24,
                                        46
                                       )
                            );
    Assertions.assertEquals(a,
                            parser.parse(a.toString())
                           );
  }

  @Test
  public void test_number_error() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  decimal()
                                 );
    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsStr.of("a")
                                                       )
                                                    .toString())
                           );

  }


  @Test
  public void test_bytes_parser() {

    final JsObjSpec objSpec = JsObjSpec.of("a",
                                           JsSpecs.str(s -> s.length() <= 10)
                                          )
                                       .lenient()
                                       .withOptKeys("a");
    JsObjSpecParser objParser = JsObjSpecParser.of(objSpec);

    JsObjGen objGen = JsObjGen.of("a",
                                  JsStrGen.biased(0,
                                                  10
                                                 ),
                                  "b",
                                  JsIntGen.biased(),
                                  "c",
                                  JsBigDecGen.biased(),
                                  "d",
                                  BytesGen.biased(0,
                                                  10
                                                 )
                                          .map(it -> JsStr.of(Base64.getEncoder()
                                                                    .encodeToString(it))),
                                  "e",
                                  BytesGen.biased(0,
                                                  100
                                                 )
                                          .map(it -> JsStr.of(Base64.getEncoder()
                                                                    .encodeToString(it)))
                                 )
                              .withOptKeys("a")
                              .withNullValues("d",
                                              "e"
                                             );

    Assertions.assertTrue(objGen.sample(10000)
                                .allMatch(v -> objParser.parse(v.toString()
                                                                .getBytes(StandardCharsets.UTF_8))
                                                        .equals(v)));

    Assertions.assertTrue(objGen.sample(10000)
                                .allMatch(v -> objParser.parse(new ByteArrayInputStream(v.toString()
                                                                                         .getBytes(StandardCharsets.UTF_8)))
                                                        .equals(v)));


  }

  @Test
  public void test_numbers() {

    String obj = """
        {\s
          "a": 10E-5,
          "b": -10E+3,
          "c":  10E-3,
          "d": 15.5E-3,
          "e": -1000.50,
          "f": 100000000000000000000000000.0E5,
          "i": 100000000000000000000000000.0E+5,
          "g": -1245600.25E-2
        }""";

    final JsObj parsed = JsObjSpecParser.of(JsObjSpec.of("a",
                                                         decimal(),
                                                         "b",
                                                         decimal(),
                                                         "c",
                                                         decimal(),
                                                         "d",
                                                         decimal(),
                                                         "e",
                                                         decimal(),
                                                         "f",
                                                         decimal(),
                                                         "g",
                                                         decimal(),
                                                         "i",
                                                         decimal()
                                                        ))
                                        .parse(obj);

    Assertions.assertEquals(JsObj.parse(obj),
                            parsed
                           );
  }


  @Test
  public void testSuchThatSpecParser() {

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
                                   JsStrGen.alphanumeric(),
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
            .suchThat(this::dependencies);

    JsObjSpec spec =
        baseSpec
            .withOptKeys("a",
                         "b",
                         "c",
                         "d"
                        )
            .suchThat(this::dependencies);

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertTrue(gen.sample(10000)
                             .allMatch(o -> spec.test(parser.parse(o.toString()))
                                                .isEmpty()));

    Assertions.assertTrue(baseGen.suchThat(spec,
                                           100
                                          )
                                 .sample(10000)
                                 .allMatch(o -> spec.test(parser.parse(o.toString()))
                                                    .isEmpty()));

    JsObjSpec spec1 = baseSpec.withAllOptKeys()
                              .suchThat(this::dependencies);
    JsObjSpecParser parser1 = JsObjSpecParser.of(spec1);
    Assertions.assertTrue(baseGen.withAllOptKeys()
                                 .suchThat(spec1)
                                 .sample(10000)
                                 .allMatch(o -> spec1.test(parser1.parse(o.toString()))
                                                     .isEmpty()));


  }


  private boolean dependencies(JsObj o) {
    if (o.containsKey("a") && !o.containsKey("c")) {
      return false;
    }
    return !o.containsKey("b") || o.containsKey("d");
  }

  @Test
  public void testLenientObjectParser() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.str()
                                         .nullable()
                                 )
                              .withOptKeys("a")
                              .lenient();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObjGen objGen = JsObjGen.of("a",
                                  JsStrGen.biased(0,
                                                  10
                                                 ),
                                  "b",
                                  JsIntGen.biased(),
                                  "c",
                                  JsBigDecGen.biased(),
                                  "d",
                                  BytesGen.biased(0,
                                                  10
                                                 )
                                          .map(it -> JsStr.of(Base64.getEncoder()
                                                                    .encodeToString(it))),
                                  "e",
                                  BytesGen.biased(0,
                                                  100
                                                 )
                                          .map(it -> JsStr.of(Base64.getEncoder()
                                                                    .encodeToString(it)))
                                 )
                              .withOptKeys("a")
                              .withNullValues("a",
                                              "d",
                                              "e"
                                             );

    Assertions.assertTrue(objGen.sample(1000)
                                .allMatch(it -> parser.parse(it.toPrettyString())
                                                      .equals(it)));
  }

  @Test
  public void testLenientArrayParser() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.str()
                                         .nullable()
                                 )
                              .withOptKeys("a")
                              .lenient();

    JsSpec arraySpec = JsSpecs.arrayOfSpec(spec.nullable());

    JsArraySpecParser parser = JsArraySpecParser.of(arraySpec);

    JsObjGen objGen = JsObjGen.of("a",
                                  JsStrGen.biased(0,
                                                  10
                                                 ),
                                  "b",
                                  JsIntGen.biased(),
                                  "c",
                                  JsBigDecGen.biased(),
                                  "d",
                                  BytesGen.biased(0,
                                                  10
                                                 )
                                          .map(it -> JsStr.of(Base64.getEncoder()
                                                                    .encodeToString(it))),
                                  "e",
                                  BytesGen.biased(0,
                                                  100
                                                 )
                                          .map(it -> JsStr.of(Base64.getEncoder()
                                                                    .encodeToString(it)))
                                 )
                              .withOptKeys("a")
                              .withNullValues("a",
                                              "d",
                                              "e"
                                             );

    Assertions.assertTrue(JsArrayGen.arbitrary(objGen,
                                               0,
                                               10
                                              )
                                    .sample(1000)
                                    .allMatch(it -> parser.parse(it.toPrettyString())
                                                          .equals(it)));
  }

  @Test
  public void test1() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.bool(),
                                  "b",
                                  JsSpecs.integer()
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse("{\"a\":true,\"b\":1]")
                           );

  }

  @Test
  public void test2() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.bool(),
                                  "b",
                                  JsSpecs.integer()
                                 )
                              .suchThat(it -> it.containsKey("hi"));

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse("{\"a\":true,\"b\":1}")
                           );

  }

  @Test
  public void test3() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.any()
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse("{\"a\":fal}")
                           );

    Assertions.assertEquals(JsObj.of("a",
                                     JsArray.empty()),
                            parser.parse("{\"a\":[]}")
                           );

  }

  @Test
  public void test4() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.array(JsValue::isInt)
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse("{\"a\":[true]}")
                           );

  }

  @Test
  public void test5() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.decimal(it -> it.compareTo(BigDecimal.ONE) > 0)
                                         .nullable()
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse("{\"a\":1}")
                           );

  }


}
