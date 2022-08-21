package jsonvalues.parser;

import com.dslplatform.json.JsParserException;
import fun.gen.BytesGen;
import fun.gen.Gen;
import jsonvalues.*;
import jsonvalues.gen.*;
import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjParser {

    @Test
    public void test_parse_obj_error() {
        JsObjSpec spec = JsObjSpec.of("a",
                                           obj(a -> a.containsKey("a"))
        ).lenient();

        JsObjParser parser = new JsObjParser(spec);


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
        ).lenient();

        JsObjParser parser = new JsObjParser(spec);


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
        ).lenient();

        JsObjParser parser = new JsObjParser(spec);


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
        ).lenient();

        JsObjParser parser = new JsObjParser(spec);


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
                                           number(JsValue::isDecimal)
        ).lenient();

        JsObjParser parser = new JsObjParser(spec);


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
        ).lenient();

        JsObjParser parser = new JsObjParser(spec);


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
                                           decimal(i -> i.divide(BigDecimal.TEN).compareTo(new BigDecimal(1)) == 0)
        ).lenient();

        JsObjParser parser = new JsObjParser(spec);


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
                                                TRUE,
                                                "f",
                                                FALSE.nullable(),
                                                "g",
                                                decimal(),
                                                "h",
                                                bigInteger(),
                                                "i",
                                                JsObjSpec.of("a",
                                                                 number(),
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
                                                                 tuple(number(JsValue::isDecimal),
                                                                       any()
                                                                 )
                                                )
        ).withOptKeys("f",
                      "i");


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
                                new JsObjParser(spec).parse(obj.toPrettyString())
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
                                                arrayOfNumber(),
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
                                                ).withOptKeys("g")
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
                                new JsObjParser(spec).parse(obj.toPrettyString())
        );

    }


    @Test
    public void test_parse_obj_all_primitive_types_with_predicates() {

        final JsObjSpec spec =
                JsObjSpec.of("a",
                                 integer(i -> i > 0),
                                 "b",
                                 str(a -> a.length() > 0),
                                 "c",
                                 longInteger(c -> c > 0),
                                 "d",
                                 bool(),
                                 "e",
                                 TRUE.nullable(),
                                 "f",
                                 FALSE.nullable(),
                                 "g",
                                 decimal(d -> d.doubleValue() > 0.0),
                                 "h",
                                 bigInteger(i -> i.longValueExact() % 2 == 0),
                                 "i",
                                 JsObjSpec.of("a",
                                                  number(),
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
                                                  tuple(number(JsValue::isDecimal),
                                                        any()
                                                  )
                                 )
                ).withOptKeys("e");


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
                                new JsObjParser(spec).parse(obj.toPrettyString())
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
        ).withOptKeys("a",
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
                      "j");


        Assertions.assertEquals(JsObj.empty(),
                                new JsObjParser(spec).parse(JsObj.empty()
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
                                new JsObjParser(spec).parse(obj.toPrettyString())
        );
    }

    @Test
    public void test_strict_mode() {
        final JsObjSpec spec = JsObjSpec.of("a",
                                                 str()
                                                         .nullable()
        ).lenient().withOptKeys("a");


        final JsObj obj = JsObj.of("b",
                                   JsStr.of("b")
        );

        final JsObj parsed = new JsObjParser(spec).parse(obj.toPrettyString());

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
        ).withOptKeys("b",
                      "e");

        final JsObj a = JsObj.of("a",
                                 JsNull.NULL,
                                 "c",
                                 JsInt.of(3),
                                 "d",
                                 JsNull.NULL,
                                 "f",
                                 JsNull.NULL
        );
        final JsObjParser parser = new JsObjParser(isint);
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
        ).withOptKeys("b",
                      "e");

        final JsObj a = JsObj.of("a",
                                 JsNull.NULL,
                                 "c",
                                 JsLong.of(3L),
                                 "d",
                                 JsNull.NULL,
                                 "f",
                                 JsNull.NULL
        );
        final JsObjParser parser = new JsObjParser(isint);
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
        ).withOptKeys("b",
                      "e");

        final JsObj a = JsObj.of("a",
                                 JsNull.NULL,
                                 "c",
                                 JsBigDec.of(new BigDecimal(3L)),
                                 "d",
                                 JsNull.NULL,
                                 "f",
                                 JsNull.NULL
        );
        final JsObjParser parser = new JsObjParser(isdec);
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
        ).withOptKeys("b",
                      "e");

        final JsObj a = JsObj.of("a",
                                 JsNull.NULL,
                                 "c",
                                 JsInt.of(3),
                                 "d",
                                 JsNull.NULL,
                                 "f",
                                 JsNull.NULL
        );
        final JsObjParser parser = new JsObjParser(isint);
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
    public void test_number_spec() {

        JsObjSpec isint = JsObjSpec.of("a",
                                           number(JsValue::isDecimal).nullable(),
                                           "b",
                                           number(JsValue::isIntegral).nullable(),
                                           "c",
                                           number(JsValue::isIntegral),
                                           "d",
                                           number(JsValue::isIntegral).nullable(),
                                           "e",
                                           number(JsValue::isDecimal).nullable(),
                                           "f",
                                           number(JsValue::isIntegral).nullable(),
                                           "g",
                                           array().nullable()
        ).withOptKeys("b",
                      "e");

        final JsObj a = JsObj.of("a",
                                 JsNull.NULL,
                                 "c",
                                 JsInt.of(3),
                                 "d",
                                 JsNull.NULL,
                                 "f",
                                 JsNull.NULL,
                                 "g",
                                 JsNull.NULL
        );
        final JsObjParser parser = new JsObjParser(isint);
        Assertions.assertEquals(a,
                                parser.parse(a.toString())
        );

        final JsObj b = JsObj.of("a",
                                 JsDouble.of(1.5),
                                 "b",
                                 JsInt.of(2),
                                 "c",
                                 JsInt.of(3),
                                 "d",
                                 JsInt.of(-5),
                                 "e",
                                 JsDouble.of(11.5),
                                 "f",
                                 JsInt.of(20),
                                 "g",
                                 JsArray.empty()
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
        ).withOptKeys("b",
                      "e");

        final JsObj a = JsObj.of("a",
                                 JsNull.NULL,
                                 "c",
                                 JsStr.of("abcd"),
                                 "d",
                                 JsNull.NULL,
                                 "f",
                                 JsNull.NULL
        );
        final JsObjParser parser = new JsObjParser(isint);
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

        JsObjParser parser = new JsObjParser(spec);

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

        JsObjParser parser = new JsObjParser(spec);

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

        JsObjParser parser = new JsObjParser(spec);

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
    public void array_of_number() {
        JsObjSpec spec = JsObjSpec.of("a",
                                          arrayOfNumber(JsValue::isDecimal),
                                          "b",
                                          arrayOfNumber(JsValue::isDecimal).nullable(),
                                          "c",
                                          arrayOfNumber(JsValue::isIntegral),
                                          "e",
                                          arrayOfNumber(JsValue::isDecimal)
        ).withOptKeys("c",
                      "e");

        JsObjParser parser = new JsObjParser(spec);

        final JsObj a = JsObj.of("a",
                                 JsArray.of(1.5,
                                            1.7
                                 ),
                                 "b",
                                 JsNull.NULL,
                                 "c",
                                 JsArray.of(1,
                                            2,
                                            3
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
        ).withOptKeys("c",
                      "e");

        JsObjParser parser = new JsObjParser(spec);

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
                                                              )).toPrettyString())
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
                                                              )).toPrettyString())
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
        ).withOptKeys("c",
                      "e");

        JsObjParser parser = new JsObjParser(spec);

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
                                          number(JsValue::isDouble)
        );
        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsObj.of("a",
                                                            JsInt.of(1)
                                                        )
                                                        .toString())
        );

    }


    @Test
    public void test_bytes_parser() {

        final JsObjSpec objSpec = JsObjSpec.of("a",
                                                    JsSpecs.str(s -> s.length() <= 10)
                                           ).lenient()
                                           .withOptKeys("a");
        JsObjParser objParser = new JsObjParser(objSpec);

        JsObjGen objGen = JsObjGen.of("a",
                                      JsStrGen.biased(0,
                                                      10),
                                      "b",
                                      JsIntGen.biased(),
                                      "c",
                                      JsBigDecGen.biased(),
                                      "d",
                                      BytesGen.biased(0,
                                                      10)
                                              .map(it -> JsStr.of(Base64.getEncoder().encodeToString(it))),
                                      "e",
                                      BytesGen.biased(0,
                                                      100).map(it -> JsStr.of(Base64.getEncoder().encodeToString(it)))
                                  )
                                  .withOptKeys("a")
                                  .withNullValues("d",
                                                  "e");

        Assertions.assertTrue(objGen.sample(10000)
                                    .allMatch(v -> objParser.parse(v.toString()
                                                                    .getBytes())
                                                            .equals(v)));

        Assertions.assertTrue(objGen.sample(10000)
                                    .allMatch(v -> objParser.parse(new ByteArrayInputStream(v.toString()
                                                                                             .getBytes()))
                                                            .equals(v)));


    }

    @Test
    public void test_numbers() {

        String obj = "{\n"
                + "  \"a\": 10E-5,\n"
                + "  \"b\": -10E+3,\n"
                + "  \"c\":  10E-3,\n"
                + "  \"d\": 15.5E-3,\n"
                + "  \"e\": -1000.50,\n"
                + "  \"f\": 100000000000000000000000000.0E5,\n"
                + "  \"i\": 100000000000000000000000000.0E+5,\n"
                + "  \"g\": -1245600.25E-2\n"
                +
                "}";

        final JsObj parsed = new JsObjParser(JsObjSpec.of("a",
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
        )).parse(obj);

        Assertions.assertEquals(JsObj.parse(obj),
                                parsed
        );
    }


    @Test
    public void testYamlToObj() {
        //language=yaml
        String yaml = "Resources:\n" +
                "  Ec2Instance:\n" +
                "    Type: 'AWS::EC2::Instance'\n" +
                "    Properties:\n" +
                "      SecurityGroups:\n" +
                "        - !Ref InstanceSecurityGroup\n" +
                "      KeyName: mykey\n" +
                "      ImageId: ''\n" +
                "  InstanceSecurityGroup:\n" +
                "    Type: 'AWS::EC2::SecurityGroup'\n" +
                "    Properties:\n" +
                "      GroupDescription: Enable SSH access via port 22\n" +
                "      SecurityGroupIngress:\n" +
                "        - IpProtocol: tcp\n" +
                "          FromPort: '22'\n" +
                "          ToPort: 22\n" +
                "          CidrIp: 0.0.0.0/0";

        JsObj obj = JsObj.parseYaml(yaml);

        Assertions.assertEquals("AWS::EC2::SecurityGroup",
                                obj.getStr(JsPath.path("/Resources/InstanceSecurityGroup/Type")));
        Assertions.assertEquals("InstanceSecurityGroup",
                                obj.getStr(JsPath.path("/Resources/Ec2Instance/Properties/SecurityGroups/0")));
        Assertions.assertEquals("0.0.0.0/0",
                                obj.getStr(JsPath.path("/Resources/InstanceSecurityGroup/Properties/SecurityGroupIngress/0/CidrIp")));

        Assertions.assertEquals(Integer.valueOf(22),
                                obj.getInt(JsPath.path("/Resources/InstanceSecurityGroup/Properties/SecurityGroupIngress/0/ToPort")));
    }

    @Test
    public void testYamlToArray() {
        String yaml = "- Resources:\n" +
                "    Ec2Instance:\n" +
                "      Type: 'AWS::EC2::Instance'\n" +
                "      Properties:\n" +
                "        SecurityGroups:\n" +
                "          - InstanceSecurityGroup\n" +
                "        KeyName: mykey\n" +
                "        ImageId: ''\n" +
                "    InstanceSecurityGroup:\n" +
                "      Type: 'AWS::EC2::SecurityGroup'\n" +
                "      Properties:\n" +
                "        GroupDescription: Enable SSH access via port 22\n" +
                "        SecurityGroupIngress:\n" +
                "          - IpProtocol: tcp\n" +
                "            FromPort: '22'\n" +
                "            ToPort: 22\n" +
                "            CidrIp: 0.0.0.0/0";


        JsArray arr = JsArray.parseYaml(yaml);

        Assertions.assertEquals("AWS::EC2::SecurityGroup",
                                arr.getStr(JsPath.path("/0/Resources/InstanceSecurityGroup/Type")));
        Assertions.assertEquals("InstanceSecurityGroup",
                                arr.getStr(JsPath.path("/0/Resources/Ec2Instance/Properties/SecurityGroups/0")));
        Assertions.assertEquals("0.0.0.0/0",
                                arr.getStr(JsPath.path("/0/Resources/InstanceSecurityGroup/Properties/SecurityGroupIngress/0/CidrIp")));
        Assertions.assertEquals(Integer.valueOf(22),
                                arr.getInt(JsPath.path("/0/Resources/InstanceSecurityGroup/Properties/SecurityGroupIngress/0/ToPort")));
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
                                      "d")
                        .suchThat(o -> dependencies(o));

        JsObjSpec spec =
                baseSpec
                        .withOptKeys("a",
                                     "b",
                                     "c",
                                     "d")
                        .suchThat(o -> dependencies(o));

        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(o -> spec.test(parser.parse(o.toString())).isEmpty()));

        Assertions.assertTrue(baseGen.suchThat(spec,
                                               100).sample(10000)
                                     .allMatch(o -> spec.test(parser.parse(o.toString())).isEmpty()));

        JsObjSpec spec1 = baseSpec.withAllOptKeys().suchThat(o -> dependencies(o));
        JsObjParser parser1 = new JsObjParser(spec1);
        Assertions.assertTrue(baseGen.withAllOptKeys()
                                     .suchThat(spec1).sample(10000)
                                     .allMatch(o -> spec1.test(parser1.parse(o.toString())).isEmpty()));


    }


    private boolean dependencies(JsObj o) {
        if (o.containsKey("a") && !o.containsKey("c")) return false;
        if (o.containsKey("b") && !o.containsKey("d")) return false;
        return true;
    }

    @Test
    public void testLenientObjectParser() {

        JsObjSpec spec = JsObjSpec.of("a",
                                           JsSpecs.str().nullable()).withOptKeys("a").lenient();

        JsObjParser parser = new JsObjParser(spec);

        JsObjGen objGen = JsObjGen.of("a",
                                      JsStrGen.biased(0,
                                                      10),
                                      "b",
                                      JsIntGen.biased(),
                                      "c",
                                      JsBigDecGen.biased(),
                                      "d",
                                      BytesGen.biased(0,
                                                      10)
                                              .map(it -> JsStr.of(Base64.getEncoder().encodeToString(it))),
                                      "e",
                                      BytesGen.biased(0,
                                                      100).map(it -> JsStr.of(Base64.getEncoder().encodeToString(it)))
                                  )
                                  .withOptKeys("a")
                                  .withNullValues("a",
                                                  "d",
                                                  "e");

        Assertions.assertTrue(objGen.sample(1000).allMatch(it -> parser.parse(it.toPrettyString()).equals(it)));
    }

    @Test
    public void testLenientArrayParser() {

        JsObjSpec spec = JsObjSpec.of("a",
                                           JsSpecs.str().nullable()).withOptKeys("a").lenient();


        JsArraySpec arraySpec = JsSpecs.arrayOfObjSpec(spec.nullable());

        JsArrayParser parser = new JsArrayParser(arraySpec);

        JsObjGen objGen = JsObjGen.of("a",
                                      JsStrGen.biased(0,
                                                      10),
                                      "b",
                                      JsIntGen.biased(),
                                      "c",
                                      JsBigDecGen.biased(),
                                      "d",
                                      BytesGen.biased(0,
                                                      10)
                                              .map(it -> JsStr.of(Base64.getEncoder().encodeToString(it))),
                                      "e",
                                      BytesGen.biased(0,
                                                      100).map(it -> JsStr.of(Base64.getEncoder().encodeToString(it)))
                                  )
                                  .withOptKeys("a")
                                  .withNullValues("a",
                                                  "d",
                                                  "e");

        Assertions.assertTrue(JsArrayGen.arbitrary(objGen,
                                                   0,
                                                   10).sample(1000).allMatch(it -> parser.parse(it.toPrettyString()).equals(it)));
    }

    @Test
    public void test1() {
        JsObjSpec spec = JsObjSpec.of("a",
                                          JsSpecs.bool(),
                                          "b",
                                          JsSpecs.integer());

        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("{\"a\":true,\"b\":1]"));

    }

    @Test
    public void test2() {
        JsObjSpec spec = JsObjSpec.of("a",
                                          JsSpecs.bool(),
                                          "b",
                                          JsSpecs.integer()).suchThat(it -> it.containsKey("hi"));

        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("{\"a\":true,\"b\":1}"));

    }

    @Test
    public void test3() {
        JsObjSpec spec = JsObjSpec.of("a",
                                          JsSpecs.any());

        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("{\"a\":fal}"));

        Assertions.assertEquals(JsObj.of("a",JsArray.empty()),
                                parser.parse("{\"a\":[]}"));

    }

    @Test
    public void test4() {
        JsObjSpec spec = JsObjSpec.of("a",
                                          JsSpecs.array(it->it.isInt()));

        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("{\"a\":[true]}"));

    }

    @Test
    public void test5() {
        JsObjSpec spec = JsObjSpec.of("a",
                                          JsSpecs.number(it->it.isInt() && it.toJsInt().value > 1).nullable());

        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("{\"a\":1}"));

    }


}
