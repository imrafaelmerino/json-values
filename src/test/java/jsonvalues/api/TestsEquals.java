package jsonvalues.api;

import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestsEquals {


    @Test
    public void testJsBigIntEquals() {

        Assertions.assertEquals(JsBigInt.of(BigInteger.TEN),
                                JsDouble.of(10d));
        Assertions.assertEquals(JsBigInt.of(BigInteger.TEN),
                                JsInt.of(10));

    }

    @Test
    public void testJsDoubleEquals() {

        Assertions.assertEquals(JsDouble.of(1.00d),
                                JsInt.of(1));

        Assertions.assertEquals(JsDouble.of(1.00d),
                                JsLong.of(1));

        Assertions.assertEquals(JsDouble.of(1.00d),
                                JsBigDec.of(BigDecimal.ONE));

        Assertions.assertEquals(JsDouble.of(1.00d),
                                JsBigInt.of(BigInteger.ONE));
    }


}
