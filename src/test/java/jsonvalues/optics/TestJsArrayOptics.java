package jsonvalues.optics;


import fun.optic.Lens;
import fun.optic.Option;
import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

public class TestJsArrayOptics {

    @Test
    public void testBigIntLensesByPath() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsBigInt.of(BigInteger.TEN)
        );

        Lens<JsArray, BigInteger> lens = JsArray.lens.integralNum(path);

        Assertions.assertEquals(BigInteger.TEN,
                                lens.get.apply(a));


        JsArray b = lens.set.apply(BigInteger.ONE)
                            .apply(a);

        Assertions.assertEquals(BigInteger.ONE,
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i.pow(2))
                               .apply(a);

        Assertions.assertEquals(BigInteger.valueOf(100),
                                lens.get.apply(c));


    }

    @Test
    public void testBigIntLenses() {

        JsArray a = JsArray.of(BigInteger.TEN,
                               BigInteger.ONE);

        Lens<JsArray, BigInteger> lens = JsArray.lens.integralNum(0);

        Assertions.assertEquals(BigInteger.TEN,
                                lens.get.apply(a));


        JsArray b = lens.set.apply(BigInteger.ONE)
                            .apply(a);

        Assertions.assertEquals(JsArray.of(BigInteger.ONE,
                                           BigInteger.ONE),
                                b);

        Assertions.assertEquals(BigInteger.ONE,
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i.pow(2))
                               .apply(a);

        Assertions.assertEquals(BigInteger.valueOf(100),
                                lens.get.apply(c));


    }


    @Test
    public void testStrLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsStr.of("abc")
        );

        Lens<JsArray, String> lens = JsArray.lens.str(path);

        Assertions.assertEquals("abc",
                                lens.get.apply(a));


        JsArray b = lens.set.apply("abcd")
                            .apply(a);

        Assertions.assertEquals("abcd",
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(String::toUpperCase)
                               .apply(a);

        Assertions.assertEquals("ABC",
                                lens.get.apply(c));


    }


    @Test
    public void testDoubleLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsDouble.of(1.5)
        );

        Lens<JsArray, Double> lens = JsArray.lens.doubleNum(path);

        Assertions.assertEquals(Double.valueOf(1.5),
                                lens.get.apply(a));


        JsArray b = lens.set.apply(10.5)
                            .apply(a);

        Assertions.assertEquals(Double.valueOf(10.5),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i + 1.0)
                               .apply(b);

        Assertions.assertEquals(Double.valueOf(11.5),
                                lens.get.apply(c));


    }

    @Test
    public void testLongLenses() {

        JsArray a = JsArray.of(JsNull.NULL,
                               JsLong.of(Long.MAX_VALUE)
        );

        Lens<JsArray, Long> lens = JsArray.lens.longNum(1);

        Assertions.assertEquals(Long.valueOf(Long.MAX_VALUE),
                                lens.get.apply(a));

        JsArray b = lens.set.apply(Long.MIN_VALUE)
                            .apply(a);

        Assertions.assertEquals(Long.valueOf(Long.MIN_VALUE),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i + 1)
                               .apply(b);

        Assertions.assertEquals(Long.valueOf(Long.MIN_VALUE + 1),
                                lens.get.apply(c));


    }

    @Test
    public void testLongLensesByPath() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsLong.of(Long.MAX_VALUE)
        );

        Lens<JsArray, Long> lens = JsArray.lens.longNum(path);

        Assertions.assertEquals(Long.valueOf(Long.MAX_VALUE),
                                lens.get.apply(a));


        JsArray b = lens.set.apply(Long.MIN_VALUE)
                            .apply(a);

        Assertions.assertEquals(Long.valueOf(Long.MIN_VALUE),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i + 1)
                               .apply(b);

        Assertions.assertEquals(Long.valueOf(Long.MIN_VALUE + 1),
                                lens.get.apply(c));


    }

    @Test
    public void testIntegerLenses() {

        JsArray a = JsArray.of(0,
                               Integer.MAX_VALUE);

        Lens<JsArray, Integer> lens = JsArray.lens.intNum(1);

        Assertions.assertEquals(Integer.valueOf(Integer.MAX_VALUE),
                                lens.get.apply(a));


        JsArray b = lens.set.apply(Integer.MIN_VALUE)
                            .apply(a);

        Assertions.assertEquals(b,
                                JsArray.of(0,
                                           Integer.MIN_VALUE));

        Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i + 1)
                               .apply(b);

        Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE + 1),
                                lens.get.apply(c));


    }

    @Test
    public void testIntegerLensesByPath() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsInt.of(Integer.MAX_VALUE)
        );

        Lens<JsArray, Integer> lens = JsArray.lens.intNum(path);

        Assertions.assertEquals(Integer.valueOf(Integer.MAX_VALUE),
                                lens.get.apply(a));


        JsArray b = lens.set.apply(Integer.MIN_VALUE)
                            .apply(a);

        Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i + 1)
                               .apply(b);

        Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE + 1),
                                lens.get.apply(c));


    }


    @Test
    public void testBooleanLensesByPath() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsBool.TRUE
        );

        Lens<JsArray, Boolean> lens = JsArray.lens.bool(path);

        Assertions.assertTrue(lens.get.apply(a));


        JsArray b = lens.set.apply(false)
                            .apply(a);

        Assertions.assertFalse(lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> !i)
                               .apply(b);

        Assertions.assertTrue(lens.get.apply(c));


    }

    @Test
    public void testBooleanLenses() {

        JsArray a = JsArray.of(
                JsBool.TRUE,
                JsNull.NULL
        );

        Lens<JsArray, Boolean> lens = JsArray.lens.bool(0);

        Assertions.assertTrue(lens.get.apply(a));


        JsArray b = lens.set.apply(false)
                            .apply(a);

        Assertions.assertEquals(JsArray.of(JsBool.FALSE,
                                           JsNull.NULL),
                                b);

        Assertions.assertFalse(lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> !i)
                               .apply(b);

        Assertions.assertTrue(lens.get.apply(c));


    }

    @Test
    public void testDecimalLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsBigDec.of(new BigDecimal("1.11"))
        );

        Lens<JsArray, BigDecimal> lens = JsArray.lens.decimalNum(path);

        Assertions.assertEquals(new BigDecimal("1.11"),
                                lens.get.apply(a));


        JsArray b = lens.set.apply(new BigDecimal("10.11"))
                            .apply(a);

        Assertions.assertEquals(new BigDecimal("10.11"),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i.plus().add(BigDecimal.valueOf(10.0)))
                               .apply(b);

        Assertions.assertEquals(new BigDecimal("20.11"),
                                lens.get.apply(c));


    }


    @Test
    public void testBoolLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsBool.TRUE
        );

        Lens<JsArray, Boolean> lens = JsArray.lens.bool(path);

        Assertions.assertEquals(true,
                                lens.get.apply(a));


        JsArray b = lens.set.apply(false)
                            .apply(a);

        Assertions.assertEquals(false,
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> !i)
                               .apply(b);

        Assertions.assertEquals(true,
                                lens.get.apply(c));


    }

    @Test
    public void testComposeArr() {

        JsArray arr = JsArray.of(JsArray.of(JsArray.of("a",
                                                       "b"),
                                            JsArray.of("c",
                                                       "d")
                                 )
        );


        Lens<JsArray, JsArray> firstArr = JsArray.lens.array(0);
        Lens<JsArray, String> a = firstArr.compose(firstArr)
                                          .compose(JsArray.lens.str(0));

        Assertions.assertEquals("a",
                                a.get.apply(arr));

        Assertions.assertEquals("hi",
                                a.get.apply(a.set.apply("hi").apply(arr)));

        JsArray newArr = a.modify.apply(String::toUpperCase).apply(arr);
        Assertions.assertEquals("A",
                                a.get.apply(newArr));

        Assertions.assertEquals("a",
                                firstArr.compose(JsArray.lens
                                                         .str(JsPath.path("/0/0")))
                                        .get.apply(arr)
        );


    }


    @Test
    public void testObjLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsObj.empty()
        );

        Lens<JsArray, JsObj> lens = JsArray.lens.obj(path);

        Assertions.assertEquals(JsObj.empty(),
                                lens.get.apply(a));


        JsArray b = lens.set.apply(JsObj.of("a",
                                            JsInt.of(1)))
                            .apply(a);

        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1)),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i.set("b",
                                                 JsStr.of("hi")))
                               .apply(b);

        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1)).set("b",
                                                          JsStr.of("hi")),
                                lens.get.apply(c));


    }

    @Test
    public void testArrayLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsArray.empty()
        );

        Lens<JsArray, JsArray> lens = JsArray.lens.array(path);

        Assertions.assertEquals(JsArray.empty(),
                                lens.get.apply(a));


        JsArray b = lens.set.apply(JsArray.empty().append(JsInt.of(1)))
                            .apply(a);

        Assertions.assertEquals(JsArray.empty().append(JsInt.of(1)),
                                lens.get.apply(b));

        JsArray c = lens.modify.apply(i -> i.append(JsInt.of(2)))
                               .apply(b);

        Assertions.assertEquals(JsArray.empty().append(JsInt.of(1),
                                                       JsInt.of(2)),
                                lens.get.apply(c));


    }

    @Test
    public void testArrayBinaryLensesByPath() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsBinary.of("hi".getBytes(StandardCharsets.UTF_8)));

        Lens<JsArray, byte[]> lens = JsArray.lens.binary(path);

        Assertions.assertArrayEquals("hi".getBytes(StandardCharsets.UTF_8),
                                     lens.get.apply(a));


    }

    @Test
    public void testArrayBinaryLenses() {

        JsArray a = JsArray.of(JsBinary.of("hi".getBytes(StandardCharsets.UTF_8)));

        Lens<JsArray, byte[]> lens = JsArray.lens.binary(0);

        Assertions.assertArrayEquals("hi".getBytes(StandardCharsets.UTF_8),
                                     lens.get.apply(a));


    }

    @Test
    public void testArrayInstantLensesByPath() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsInstant.of(Instant.MAX));

        Lens<JsArray, Instant> lens = JsArray.lens.instant(path);

        Assertions.assertEquals(Instant.MAX,
                                lens.get.apply(a));


    }

    @Test
    public void testArrayInstantLenses() {

        JsArray a = JsArray.of(JsInstant.of(Instant.MAX));

        Lens<JsArray, Instant> lens = JsArray.lens.instant(0);

        Assertions.assertEquals(Instant.MAX,
                                lens.get.apply(a));

    }

    @Test
    public void testArrayStrLensesByPath() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.empty().set(path,
                                        JsStr.of("a"));

        Lens<JsArray, String> lens = JsArray.lens.str(path);

        Assertions.assertEquals("a",
                                lens.get.apply(a));


    }

    @Test
    public void testArrayStrLenses() {

        JsArray a = JsArray.of(JsStr.of("a"));

        Lens<JsArray, String> lens = JsArray.lens.str(0);

        Assertions.assertEquals("a",
                                lens.get.apply(a));

    }

    @Test
    public void testArrayStrOption() {

        JsArray a = JsArray.of(JsStr.of("a"));

        Option<JsArray, String> lens = JsArray.optional.str(0);

        Assertions.assertEquals("a",
                                lens.get.apply(a).get());

        Assertions.assertEquals(Optional.empty(),
                                lens.get.apply(JsArray.empty()));

    }

    @Test
    public void testArrayIntOption() {

        JsArray a = JsArray.of(JsInt.of(1));

        Option<JsArray, Integer> lens = JsArray.optional.intNum(0);

        Assertions.assertEquals(1,
                                lens.get.apply(a).get());

        Assertions.assertEquals(Optional.empty(),
                                lens.get.apply(JsArray.of("a")));

    }

    @Test
    public void testArrayLongOption() {

        JsArray a = JsArray.of(JsLong.of(Long.MAX_VALUE));

        Option<JsArray, Long> lens = JsArray.optional.longNum(0);

        Assertions.assertEquals(Long.MAX_VALUE,
                                lens.get.apply(a).get());

        Assertions.assertEquals(Optional.empty(),
                                lens.get.apply(JsArray.of("a")));

    }

    @Test
    public void testArrayBigIntOption() {

        JsArray a = JsArray.of(JsBigInt.of(BigInteger.TEN));

        Option<JsArray, BigInteger> lens = JsArray.optional.integralNum(0);

        Assertions.assertEquals(BigInteger.TEN,
                                lens.get.apply(a).get());

        Assertions.assertEquals(Optional.empty(),
                                lens.get.apply(JsArray.of("a")));

    }

    @Test
    public void testArrayBigDecOption() {

        JsArray a = JsArray.of(JsBigDec.of(BigDecimal.TEN));

        Option<JsArray, BigDecimal> lens = JsArray.optional.decimalNum(0);

        Assertions.assertEquals(BigDecimal.TEN,
                                lens.get.apply(a).get());

        Assertions.assertEquals(Optional.empty(),
                                lens.get.apply(JsArray.of("a")));

    }

    @Test
    public void testArrayBoolOption() {

        JsArray a = JsArray.of(JsBool.TRUE);

        Option<JsArray, Boolean> lens = JsArray.optional.bool(0);

        Assertions.assertEquals(true,
                                lens.get.apply(a).get());

        Assertions.assertEquals(Optional.empty(),
                                lens.get.apply(JsArray.of("a")));

    }

    @Test
    public void testArrayDoubleOption() {

        JsArray a = JsArray.of(JsDouble.of(1.5d));

        Option<JsArray, Double> lens = JsArray.optional.doubleNum(0);

        Assertions.assertEquals(1.5d,
                                lens.get.apply(a).get());

        Assertions.assertEquals(Optional.empty(),
                                lens.get.apply(JsArray.of("a")));

    }

}
