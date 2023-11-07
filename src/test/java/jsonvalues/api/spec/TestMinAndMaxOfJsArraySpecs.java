package jsonvalues.api.spec;

import jsonvalues.*;
import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestMinAndMaxOfJsArraySpecs {


    @Test
    public void testArrayOfBool() {


        JsSpec spec = JsSpecs.arrayOfBool(1,
                                          2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
        JsSpec spec = JsSpecs.arrayOfStr(1,
                                         2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
        JsSpec spec = JsSpecs.arrayOfInt(1,
                                         2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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

        JsDouble ONE = JsDouble.of(1.0);
        JsSpec spec = JsSpecs.arrayOfDouble(1,
                                            2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
        JsSpec spec = JsSpecs.arrayOfLong(1,
                                          2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
        JsSpec spec = JsSpecs.arrayOfObj(1,
                                         2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
        JsSpec spec = JsSpecs.arrayOfDec(1,
                                         2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
        JsSpec spec = JsSpecs.arrayOfBigInt(1,
                                            2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
        JsSpec spec = JsSpecs.arrayOfSpec(JsObjSpec.of("a",
                                                       JsSpecs.integer()),
                                          1,
                                          2);

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

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
