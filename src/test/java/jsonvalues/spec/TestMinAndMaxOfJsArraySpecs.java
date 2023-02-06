package jsonvalues.spec;

import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestMinAndMaxOfJsArraySpecs {


    @Test
    public void testArrayOfBool() {


        JsArraySpec spec = JsSpecs.arrayOfBool(1,
                                               2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(JsBool.TRUE,
                                                              JsBool.TRUE,
                                                              JsBool.TRUE).toString()));

        Assertions.assertEquals(JsArray.of(JsBool.TRUE),
                                parser.parse(JsArray.of(JsBool.TRUE).toString()));

        Assertions.assertEquals(JsArray.of(JsBool.TRUE,
                                           JsBool.TRUE),
                                parser.parse(JsArray.of(JsBool.TRUE,
                                                        JsBool.TRUE).toString()));
    }

    @Test
    public void testArrayOfString() {

        JsStr A = JsStr.of("a");
        JsArraySpec spec = JsSpecs.arrayOfStr(1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(A,
                                                              A,
                                                              A).toString()));

        Assertions.assertEquals(JsArray.of(A),
                                parser.parse(JsArray.of(A).toString()));

        Assertions.assertEquals(JsArray.of(A,
                                           A),
                                parser.parse(JsArray.of(A,
                                                        A).toString()));
    }


    @Test
    public void testArrayOfInt() {

        JsInt ONE = JsInt.of(1);
        JsArraySpec spec = JsSpecs.arrayOfInt(1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(ONE,
                                                              ONE,
                                                              ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE),
                                parser.parse(JsArray.of(ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE,
                                           ONE),
                                parser.parse(JsArray.of(ONE,
                                                        ONE).toString()));
    }

    @Test
    public void testArrayOfNumber() {

        JsInt ONE = JsInt.of(1);
        JsArraySpec spec = JsSpecs.arrayOfNumber(1,
                                                 2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(ONE,
                                                              ONE,
                                                              ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE),
                                parser.parse(JsArray.of(ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE,
                                           ONE),
                                parser.parse(JsArray.of(ONE,
                                                        ONE).toString()));
    }

    @Test
    public void testArrayOfLong() {

        JsLong MAX = JsLong.of(Long.MAX_VALUE);
        JsArraySpec spec = JsSpecs.arrayOfLong(1,
                                               2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(MAX,
                                                              MAX,
                                                              MAX).toString()));

        Assertions.assertEquals(JsArray.of(MAX),
                                parser.parse(JsArray.of(MAX).toString()));

        Assertions.assertEquals(JsArray.of(MAX,
                                           MAX),
                                parser.parse(JsArray.of(MAX,
                                                        MAX).toString()));
    }

    @Test
    public void testArrayOfObj() {

        JsObj EMPTY = JsObj.empty();
        JsArraySpec spec = JsSpecs.arrayOfObj(1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(EMPTY,
                                                              EMPTY,
                                                              EMPTY).toString()));

        Assertions.assertEquals(JsArray.of(EMPTY),
                                parser.parse(JsArray.of(EMPTY).toString()));

        Assertions.assertEquals(JsArray.of(EMPTY,
                                           EMPTY),
                                parser.parse(JsArray.of(EMPTY,
                                                        EMPTY).toString()));
    }

    @Test
    public void testArrayOfDec() {

        JsBigDec TEN = JsBigDec.of(BigDecimal.TEN);
        JsArraySpec spec = JsSpecs.arrayOfDec(1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(TEN,
                                                              TEN,
                                                              TEN).toString()));

        Assertions.assertEquals(JsArray.of(TEN),
                                parser.parse(JsArray.of(TEN).toString()));

        Assertions.assertEquals(JsArray.of(TEN,
                                           TEN),
                                parser.parse(JsArray.of(TEN,
                                                        TEN).toString()));
    }

    @Test
    public void testArrayOfIntegral() {

        JsBigInt A = JsBigInt.of(new BigInteger("111111111111111111111111111111111111111111111111"));
        JsArraySpec spec = JsSpecs.arrayOfBigInt(1,
                                                   2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(A,
                                                              A,
                                                              A).toString()));

        Assertions.assertEquals(JsArray.of(A),
                                parser.parse(JsArray.of(A).toString()));

        Assertions.assertEquals(JsArray.of(A,
                                           A),
                                parser.parse(JsArray.of(A,
                                                        A).toString()));
    }

    @Test
    public void testArrayOfObjSpec() {

        JsObj A = JsObj.of("a",
                           JsInt.of(1));
        JsArraySpec spec = JsSpecs.arrayOfObjSpec(JsObjSpec.of("a",
                                                                   JsSpecs.integer()),
                                                  1,
                                                  2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(A,
                                                              A,
                                                              A).toString()));

        Assertions.assertEquals(JsArray.of(A),
                                parser.parse(JsArray.of(A).toString()));

        Assertions.assertEquals(JsArray.of(A,
                                           A),
                                parser.parse(JsArray.of(A,
                                                        A).toString()));
    }
}
