import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.*;

class TestJsElems
{

    @Test
    void map_pairs()
    {

        Assertions.assertEquals(JsPair.of("a.b.2",
                                          JsInt.of(1)
                                         ),
                                JsPair.of("a.b.1",
                                          JsInt.of(1)
                                         )
                                      .mapPath(JsPath::inc)
                               );

        final JsPair pair1 = JsPair.of("a.b.1",
                                       JsInt.of(1)
                                      )
                                   .mapElem(e -> e.asJsInt()
                                                  .map(i -> i + 1));

        Assertions.assertEquals(JsPair.of("a.b.1",
                                          JsInt.of(2)
                                         ),
                                pair1
                               );

    }

    @Test
    void map_elements()
    {

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
        Assertions.assertEquals(JsBool.TRUE,
                                JsBool.FALSE.negate()
                               );

    }

    @Test
    void test_elements()
    {
        Assertions.assertTrue(JsInt.of(10)
                                   .test(i -> i % 2 == 0));
        Assertions.assertTrue(JsBigInt.of(BigInteger.ONE)
                                      .test(i -> i.equals(BigInteger.ONE)));
        Assertions.assertTrue(JsBigDec.of(BigDecimal.ONE)
                                      .test(i -> i.equals(BigDecimal.ONE)));
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
    void test_dates()
    {
        Assertions.assertTrue(JsStr.of(Instant.now()
                                              .toString())
                                   .isInstant());
        Assertions.assertFalse(JsStr.of(LocalDateTime.now()
                                                     .toString())
                                    .isInstant(i -> true));
        Assertions.assertTrue(JsStr.of(Instant.now()
                                              .toString())
                                   .isInstant(i -> i.isBefore(Instant.now()
                                                                     .plusSeconds(1))));
        Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                    .isInstant());
        Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE.format(LocalDateTime.now()))
                                   .isLocalDate(ISO_LOCAL_DATE));
        Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE.format(LocalDateTime.now()))
                                   .isLocalDate(ISO_LOCAL_DATE,
                                                it -> it.isBefore(LocalDate.now()
                                                                           .plusYears(1))
                                               ));
        Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                    .isLocalDate(ISO_LOCAL_DATE));
        Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                    .isLocalDate(ISO_LOCAL_DATE,
                                                 i -> true
                                                ));
        Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                    .isInstant());
        Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))
                                   .isLocalDateTime(ISO_LOCAL_DATE_TIME));
        Assertions.assertFalse(JsStr.of(ISO_LOCAL_DATE.format(LocalDate.now()))
                                    .isLocalDateTime(ISO_LOCAL_DATE_TIME,
                                                     i -> true
                                                    ));
        Assertions.assertFalse(JsStr.of(ISO_LOCAL_DATE.format(LocalDate.now()))
                                    .isLocalDateTime(ISO_LOCAL_DATE_TIME
                                                    ));
        Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))
                                   .isLocalDateTime(ISO_LOCAL_DATE_TIME,
                                                    it -> it.isBefore(LocalDateTime.now()
                                                                                   .plusYears(1))
                                                   ));
    }

    @Test
    void jselem_casting_exceptions()
    {
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsInt.of(1)
                                           .asJson()
                               );
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsInt.of(1)
                                           .asJsStr()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsInt.of(1)
                                           .asJsObj()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsStr.of("1")
                                           .asJsInt()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsStr.of("1")
                                           .asJsBigInt()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsStr.of("1")
                                           .asJsBigDec()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsDouble.of(1d)
                                              .asJsInt()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsDouble.of(1d)
                                              .asJsStr()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsLong.of(1)
                                            .asJsStr()
                               );


        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .asJsStr()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .asJsArray()
                               );


        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .asJsDouble()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .asJsInt()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .asJsLong()
                               );

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> JsBigInt.of(BigInteger.ONE)
                                              .asJsBool()
                               );
    }


    @Test
    void test_equals()
    {
        Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.00)),
                                JsBigDec.of(BigDecimal.ONE)
                               );
        Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.00)).hashCode(),
                                JsBigDec.of(BigDecimal.ONE).hashCode()
                               );
    }


}
