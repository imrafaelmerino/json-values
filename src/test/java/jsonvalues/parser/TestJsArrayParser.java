package jsonvalues.parser;

import jsonvalues.*;
import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsArrayParser {

    @Test
    public void test_array_of_different_elements() {

        final JsTupleSpec spec = JsSpecs.tuple(str(),
                                               integer(),
                                               longInteger(),
                                               decimal(),
                                               bool(),
                                               bigInteger(),
                                               obj(),
                                               array(),
                                               integer(i -> i > 0),
                                               any(v -> v.isStr() || v.isInt()),
                                               str(s -> s.startsWith("1")),
                                               arrayOfBigIntSuchThat(a -> a.size() == 1).nullable(),
                                               arrayOfNumberSuchThat(a -> a.size() == 2),
                                               number(JsValue::isDecimal),
                                               arrayOfBigInt(a -> a.longValueExact() > 0),
                                               arrayOfBigInt(a -> a.longValueExact() > 0).nullable(),
                                               arrayOfObj(),
                                               arrayOfObj().nullable()
        );


        JsArray array = JsArray.of(JsStr.of("a"),
                                   JsInt.of(1),
                                   JsLong.of(1),
                                   JsBigDec.of(BigDecimal.TEN),
                                   JsBool.TRUE,
                                   JsBigInt.of(BigInteger.TEN),
                                   JsObj.empty(),
                                   JsArray.empty(),
                                   JsInt.of(10),
                                   JsStr.of("a"),
                                   JsStr.of("1aaa"),
                                   JsArray.of(1),
                                   JsArray.of(1.5,
                                              2.5
                                   ),
                                   JsDouble.of(1.5),
                                   JsArray.of(1,
                                              2
                                   ),
                                   JsNull.NULL,
                                   JsArray.of(JsObj.empty(),
                                              JsObj.empty()
                                   ),
                                   JsNull.NULL
        );

        Assertions.assertEquals(array,
                                new JsArrayParser(spec).parse(array.toPrettyString())
        );
    }

    @Test
    public void testNullable() {
        JsObjSpec spec = JsObjSpec.strict("a",
                                          arrayOfStr().nullable()
        );

        JsObjParser parser = new JsObjParser(spec);
        final JsObj a = JsObj.of("a",
                                 JsArray.of("a",
                                            "b"
                                 )
        );
        Assertions.assertTrue(spec.test(a)
                                  .isEmpty());
        Assertions.assertEquals(parser.parse(a.toString()),
                                a);


        final JsObj b = JsObj.of("a",
                                 JsNull.NULL
        );
        Assertions.assertTrue(spec.test(b)
                                  .isEmpty());
        Assertions.assertEquals(parser.parse(b.toString()),
                                b);


        JsObjSpec specST = JsObjSpec.strict("a",
                                            arrayOfStrSuchThat(it -> it.size() % 2 == 0).nullable()
        );
        JsObjParser parserST = new JsObjParser(specST);

        Assertions.assertTrue(specST.test(a)
                                    .isEmpty());
        Assertions.assertEquals(parserST.parse(a.toString()),
                                a);

        Assertions.assertTrue(specST.test(b)
                                    .isEmpty());
        Assertions.assertEquals(parserST.parse(b.toString()),
                                b);

    }

    @Test
    public void testArrayOfObject() {
        JsObjSpec spec = JsObjSpec.strict("a",
                                          JsSpecs.arrayOfObjSpec(JsObjSpec.strict("a",
                                                                                  str(),
                                                                                  "b",
                                                                                  integer()
                                          ))
        );

        final JsObj a = JsObj.of("a",
                                 JsArray.of(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsInt.of(1)
                                 ))
        );


        Assertions.assertEquals(a,
                                new JsObjParser(spec).parse(a.toString())
        );

        JsObjSpec specNullable = JsObjSpec.strict("a",
                                                  JsSpecs.arrayOfObjSpec(JsObjSpec.strict("a",
                                                                                          str(),
                                                                                          "b",
                                                                                          integer()
                                                         ))
                                                         .nullable()
        );

        final JsObj b = JsObj.of("a",
                                 JsNull.NULL
        );
        Assertions.assertEquals(b,
                                new JsObjParser(specNullable).parse(b
                                                                            .toString())
        );

        JsObjSpec specTested = JsObjSpec.strict("a",
                                                arrayOfObj(o -> o.containsKey("a")
                                                )
                                                 .nullable()
        ).setOptionals("a");

        final JsObj c = JsObj.of("a",
                                 JsArray.of(JsObj.of("a",
                                                     JsBool.TRUE
                                            ),
                                            JsObj.of("a",
                                                     JsInt.of(1)
                                            )
                                 )
        );
        Assertions.assertEquals(c,
                                new JsObjParser(specTested).parse(c
                                                                          .toString())
        );

        JsObjSpec specSuchThat = JsObjSpec.strict("a",
                                                  arrayOfObjSuchThat(arr -> arr.size() > 1).nullable(),
                                                  "b",
                                                  arrayOfBoolSuchThat(arr -> arr.size() > 2)
                                                                                            .nullable(),
                                                  "c",
                                                  arrayOfNumberSuchThat(arr -> arr.head()
                                                                                  .equals(JsInt.of(1)))
                                                                                                       .nullable(),
                                                  "d",
                                                  arrayOfDecSuchThat(arr -> arr.head()
                                                                               .equals(JsBigDec.of(BigDecimal.TEN)))
                                                                                                                    .nullable(),
                                                  "e",
                                                  arrayOfLongSuchThat(arr -> arr.size() == 3)
                                                                                             .nullable(),
                                                  "f",
                                                  arrayOfIntSuchThat(arr -> arr.size() == 3),
                                                  "g",
                                                  arrayOfDec(i -> i.longValueExact() % 2 == 0),
                                                  "h",
                                                  arrayOfDec(i -> i.longValueExact() % 2 == 1).nullable(),
                                                  "i",
                                                  arrayOfStr(i -> i.length() % 2 == 0).nullable()
                                                                                      ,
                                                  "j",
                                                  arrayOfStr(i -> i.length() % 2 == 0).nullable(),
                                                  "k",
                                                  arrayOfNumber(JsValue::isDecimal),
                                                  "l",
                                                  arrayOfNumber(JsValue::isDecimal).nullable()
        ).setOptionals("a","b","c","d","e","i");

        final JsObj d = JsObj.of("a",
                                 JsArray.of(JsObj.empty(),
                                            JsObj.empty()
                                 ),
                                 "b",
                                 JsArray.of(true,
                                            true,
                                            false
                                 ),
                                 "c",
                                 JsArray.of(1,
                                            2,
                                            3
                                 ),
                                 "d",
                                 JsArray.of(JsBigDec.of(BigDecimal.TEN)),
                                 "e",
                                 JsArray.of(1L,
                                            2L,
                                            3L
                                 ),
                                 "f",
                                 JsArray.of(1,
                                            2,
                                            3
                                 ),
                                 "g",
                                 JsArray.of(4.000,
                                            10.000
                                 ),
                                 "h",
                                 JsNull.NULL,
                                 "i",
                                 JsNull.NULL,
                                 "j",
                                 JsArray.of("abce",
                                            "cdee"
                                 ),
                                 "k",
                                 JsArray.of(1.5,
                                            2.5
                                 ),
                                 "l",
                                 JsNull.NULL

        );
        Assertions.assertEquals(d,
                                new JsObjParser(specSuchThat).parse(d.toString())
        );

    }


    @Test
    public void testArrayOfValue() {

        JsObjSpec spec = JsObjSpec.lenient("a",
                                           array(),
                                           "b",
                                           array().nullable(),
                                           "c",
                                           array()
                                                .nullable(),
                                           "d",
                                           array(v -> v.isIntegral() || v.isStr())
                                                                                  .nullable(),
                                           "e",
                                           array(v -> v.isIntegral() || v.isStr()).nullable(),
                                           "f",
                                           array(v -> v.isIntegral() || v.isStr())
                                                                                  .nullable()
        ).setOptionals("c","d","f");

        JsObjParser parser = new JsObjParser(spec);

        JsObj a = JsObj.of("a",
                           JsArray.of(1,
                                      2
                           ),
                           "b",
                           JsNull.NULL,
                           "d",
                           JsArray.of(JsInt.of(10),
                                      JsStr.of("a")
                           ),
                           "e",
                           JsNull.NULL
        );

        Assertions.assertEquals(a,
                                parser.parse(a.toPrettyString())
        );
    }
}
