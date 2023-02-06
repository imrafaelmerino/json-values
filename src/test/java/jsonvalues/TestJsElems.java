package jsonvalues;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class TestJsElems {

    @Test
    public void test_dates() {
        Assertions.assertTrue(JsInstant.of(Instant.now())
                                       .isInstant());

        Assertions.assertTrue(JsInstant.of(Instant.now()
                                       )
                                       .isInstant(i -> i.isBefore(Instant.now()
                                                                         .plusSeconds(1))));

    }


    @Test
    public void test_elements() {
        Assertions.assertTrue(JsInt.of(10)
                                   .test(i -> i % 2 == 0));
        Assertions.assertTrue(JsBigInt.of(BigInteger.ONE)
                                      .test(i -> i.equals(BigInteger.ONE)));
        Assertions.assertTrue(JsBigDec.of(BigDecimal.ONE)
                                      .test(i -> i.compareTo(BigDecimal.ONE) == 0));
        Assertions.assertTrue(JsStr.of("abcd")
                                   .test(s -> s.length() == 4));

        Assertions.assertTrue(JsLong.of(20L)
                                    .test(l -> l == 20L));
        Assertions.assertTrue(JsDouble.of(0.5d)
                                      .test(d -> d == 0.5d));
        Assertions.assertTrue(JsBool.TRUE.isTrue());

        Assertions.assertTrue(JsBigInt.of(BigInteger.ONE)
                                      .isBigInt(i -> i.equals(BigInteger.ONE)));

        Assertions.assertFalse(JsDouble.of(1.5d)
                                       .test(i -> i % 2 == 0));

    }

    @Test
    @SuppressWarnings("squid:S5845")
    public void test_equals() {
        Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.00)),
                                JsBigDec.of(BigDecimal.ONE)
        );
        Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.00))
                                        .hashCode(),
                                JsBigDec.of(BigDecimal.ONE)
                                        .hashCode()
        );

        Assertions.assertEquals(JsLong.of(1L),
                                JsBigDec.of(new BigDecimal(1))
        );
        Assertions.assertEquals(JsLong.of(1L),
                                JsDouble.of(1.00)
        );
        Assertions.assertEquals(JsInt.of(1),
                                JsDouble.of(1.00)
        );

        Assertions.assertEquals(JsInt.of(1)
                                     .hashCode(),
                                JsDouble.of(1.00)
                                        .hashCode()
        );

        Assertions.assertEquals(JsBigInt.of(BigInteger.ONE)
                                        .hashCode(),
                                JsDouble.of(1.00)
                                        .hashCode()
        );

        Assertions.assertEquals(JsDouble.of(100_000_000_000d)
                                        .hashCode(),
                                JsLong.of(100_000_000_000L)
                                      .hashCode());
        Assertions.assertEquals(JsDouble.of(100_000_000_000d)
                                        .hashCode(),
                                JsBigInt.of(new BigInteger("100000000000"))
                                        .hashCode());
    }

    @Test
    public void test_jselem_casting_exceptions() {
        Assertions.assertThrows(UserError.class,
                                () -> JsInt.of(1)
                                           .toJson()
        );
        Assertions.assertThrows(UserError.class,
                                () -> JsInt.of(1)
                                           .toJsStr()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsInt.of(1)
                                           .toJsObj()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsStr.of("1")
                                           .toJsInt()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsStr.of("1")
                                           .toJsBigInt()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsStr.of("1")
                                           .toJsBigDec()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsDouble.of(1d)
                                              .toJsInt()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsDouble.of(1d)
                                              .toJsStr()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsLong.of(1)
                                            .toJsStr()
        );


        Assertions.assertThrows(UserError.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .toJsStr()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .toJsArray()
        );


        Assertions.assertThrows(UserError.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .toJsDouble()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .toJsInt()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .toJsLong()
        );

        Assertions.assertThrows(UserError.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .toJsBool()
        );
    }

    @Test
    public void test_map_elements() {

        Assertions.assertEquals(JsInt.of(10),
                                JsInt.of(5)
                                     .map(i -> i * 2)
        );
        Assertions.assertEquals(JsBigInt.of(BigInteger.valueOf(2)),
                                JsBigInt.of(BigInteger.ONE)
                                        .map(i -> i.add(BigInteger.ONE))
        );
        Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.5d)),
                                JsBigDec.of(BigDecimal.valueOf(1d))
                                        .map(i -> i.add(BigDecimal.valueOf(0.5d)))
        );
        Assertions.assertEquals(JsInt.of(10),
                                JsInt.of(5)
                                     .map(i -> i * 2)
        );
        Assertions.assertEquals(JsStr.of("abcd"),
                                JsStr.of("ABCD")
                                     .map(String::toLowerCase)
        );
        Assertions.assertEquals(JsLong.of(Long.MAX_VALUE - 2),
                                JsLong.of(2)
                                      .map(i -> Long.MAX_VALUE - 2)
        );
        Assertions.assertEquals(JsDouble.of(0.5d),
                                JsDouble.of(0.1d)
                                        .map(i -> i * 5)
        );


    }

    @Test
    public void test_json_obj() {

        byte[] hi = "hi!".getBytes(StandardCharsets.UTF_8);

        JsObj json = JsObj.of("a",
                              JsInt.of(1000),
                              "b",
                              JsBigDec.of(BigDecimal.valueOf(100_000_000_000_000L)),
                              "c",
                              JsInstant.of("2022-05-25T14:27:37.353Z"),
                              "d",
                              JsStr.of(Base64.getEncoder().encodeToString(hi))
        );

        JsObj json1 = JsObj.of("b",
                               JsBigInt.of(BigInteger.valueOf(100_000_000_000_000L)),
                               "a",
                               JsLong.of(1000L),
                               "c",
                               JsStr.of("2022-05-25T14:27:37.353Z"),
                               "d",
                               JsBinary.of(hi)
        );

        System.out.println(json.toPrettyString());


        Assertions.assertEquals(json,
                                json1);
        Assertions.assertEquals(json.hashCode(),
                                json1.hashCode());


    }
    

}
