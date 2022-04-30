package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;

public class TestOptionals {

    @Test
    public void test() {

        final Instant now = Instant.now();
        JsArray array = JsArray.of(JsLong.of(Long.MAX_VALUE),
                                   JsInt.of(Integer.MIN_VALUE),
                                   JsStr.of("hi"),
                                   JsInstant.of(now),
                                   JsBool.TRUE,
                                   JsBigDec.of(BigDecimal.valueOf(1L)),
                                   JsBigInt.of(new BigInteger(Long.MAX_VALUE+"11"))
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
        Option<JsArray, BigInteger> bigInt =
                JsOptics.array.optional.bigIntNum(6);

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

        Assertions.assertEquals(bigInt.get.apply(array),
                                Optional.of(new BigInteger(Long.MAX_VALUE+"11")));
        Assertions.assertEquals(bigInt.get.apply(JsArray.empty()),
                                Optional.empty());



    }
}
