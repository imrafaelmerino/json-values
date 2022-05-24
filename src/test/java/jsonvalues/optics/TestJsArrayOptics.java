package jsonvalues.optics;


import fun.optic.Lens;
import fun.tuple.Pair;
import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestJsArrayOptics {

    @Test
    public void testBigDecLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsBigInt.of(BigInteger.TEN)
                               )
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
    public void testStrLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsStr.of("abc")
                               )
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
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsDouble.of(1.5)
                               )
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

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsLong.of(Long.MAX_VALUE)
                               )
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

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsInt.of(Integer.MAX_VALUE)
                               )
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
    public void testBooleanLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsBool.TRUE
                               )
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
    public void testDecimalLenses() {

        JsPath path = JsPath.path("/0/b/c");
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsBigDec.of(new BigDecimal("1.11"))
                               )
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
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsBool.TRUE
                               )
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
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsObj.empty()
                               )
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
        JsArray a = JsArray.of(new Pair<>(path,
                                          JsArray.empty()
                               )
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


}
