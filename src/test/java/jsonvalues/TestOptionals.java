package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;

public class TestOptionals {

    @Test
    public void testByIndex() {

        final Instant now = Instant.now();
        JsArray array = JsArray.of(JsLong.of(Long.MAX_VALUE),
                                   JsInt.of(Integer.MIN_VALUE),
                                   JsStr.of("hi"),
                                   JsInstant.of(now),
                                   JsBool.TRUE,
                                   JsBigDec.of(BigDecimal.valueOf(1L)),
                                   JsBigInt.of(new BigInteger(Long.MAX_VALUE + "11")),
                                   JsDouble.of(0.5)
        );
        Option<JsArray, Long> longopt =
                JsOptics.array.optional.longNum(0);
        Option<JsArray, Integer> intopt =
                JsOptics.array.optional.intNum(1);
        Option<JsArray, String> stropt =
                JsOptics.array.optional.str(2);
        Option<JsArray, Instant> instantOpt =
                JsOptics.array.optional.instant(3);
        Option<JsArray, Boolean> boolopt =
                JsOptics.array.optional.bool(4);
        Option<JsArray, BigDecimal> bigDecOpt =
                JsOptics.array.optional.decimalNum(5);
        Option<JsArray, BigInteger> bigIntOpt =
                JsOptics.array.optional.bigIntNum(6);
        Option<JsArray, Double> doubleOpt =
                JsOptics.array.optional.doubleNum(7);

        Assertions.assertEquals(stropt.get.apply(array),
                                Optional.of("hi"));
        Assertions.assertEquals(stropt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(instantOpt.get.apply(array),
                                Optional.of(now));
        Assertions.assertEquals(instantOpt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(intopt.get.apply(array),
                                Optional.of(Integer.MIN_VALUE));
        Assertions.assertEquals(intopt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(longopt.get.apply(array),
                                Optional.of(Long.MAX_VALUE));
        Assertions.assertEquals(longopt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(boolopt.get.apply(array),
                                Optional.of(true));
        Assertions.assertEquals(boolopt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(bigDecOpt.get.apply(array),
                                Optional.of(BigDecimal.valueOf(1L)));
        Assertions.assertEquals(bigDecOpt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(bigIntOpt.get.apply(array),
                                Optional.of(new BigInteger(Long.MAX_VALUE + "11")));
        Assertions.assertEquals(bigIntOpt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(doubleOpt.get.apply(array),
                                Optional.of(0.5));
        Assertions.assertEquals(doubleOpt.get.apply(JsArray.empty()),
                                Optional.empty());


    }

    @Test
    public void testByPath() {

        final Instant now = Instant.now();
        JsArray a = JsArray.of(JsLong.of(Long.MAX_VALUE),
                               JsInt.of(Integer.MIN_VALUE),
                               JsStr.of("hi"),
                               JsInstant.of(now),
                               JsBool.TRUE,
                               JsBigDec.of(BigDecimal.valueOf(1L)),
                               JsBigInt.of(new BigInteger(Long.MAX_VALUE + "11")),
                               JsDouble.of(0.5)
        );
        JsArray array = JsArray.of(JsObj.of("a",
                                            JsArray.of(a)));
        Option<JsArray, Long> longopt =
                JsOptics.array.optional.longNum(JsPath.path("/0/a/0/0"));
        Option<JsArray, Integer> intopt =
                JsOptics.array.optional.intNum(JsPath.path("/0/a/0/1"));
        Option<JsArray, String> stropt =
                JsOptics.array.optional.str(JsPath.path("/0/a/0/2"));
        Option<JsArray, Instant> instantOpt =
                JsOptics.array.optional.instant(JsPath.path("/0/a/0/3"));
        Option<JsArray, Boolean> boolopt =
                JsOptics.array.optional.bool(JsPath.path("/0/a/0/4"));
        Option<JsArray, BigDecimal> bigDecOpt =
                JsOptics.array.optional.decimalNum(JsPath.path("/0/a/0/5"));
        Option<JsArray, BigInteger> bigIntOpt =
                JsOptics.array.optional.bigIntNum(JsPath.path("/0/a/0/6"));
        Option<JsArray, Double> doubleOpt =
                JsOptics.array.optional.doubleNum(JsPath.path("/0/a/0/7"));

        Assertions.assertEquals(stropt.get.apply(array),
                                Optional.of("hi"));
        Assertions.assertEquals(stropt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(instantOpt.get.apply(array),
                                Optional.of(now));
        Assertions.assertEquals(instantOpt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(intopt.get.apply(array),
                                Optional.of(Integer.MIN_VALUE));
        Assertions.assertEquals(intopt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(longopt.get.apply(array),
                                Optional.of(Long.MAX_VALUE));
        Assertions.assertEquals(longopt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(boolopt.get.apply(array),
                                Optional.of(true));
        Assertions.assertEquals(boolopt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(bigDecOpt.get.apply(array),
                                Optional.of(BigDecimal.valueOf(1L)));
        Assertions.assertEquals(bigDecOpt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(bigIntOpt.get.apply(array),
                                Optional.of(new BigInteger(Long.MAX_VALUE + "11")));
        Assertions.assertEquals(bigIntOpt.get.apply(JsArray.empty()),
                                Optional.empty());

        Assertions.assertEquals(doubleOpt.get.apply(array),
                                Optional.of(0.5));
        Assertions.assertEquals(doubleOpt.get.apply(JsArray.empty()),
                                Optional.empty());


    }
}
