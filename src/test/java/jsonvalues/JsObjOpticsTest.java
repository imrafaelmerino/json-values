package jsonvalues;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class JsObjOpticsTest {

    @Test
    public void testBigDecLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     BigInteger.TEN
                                    )
                          );

        JsValueLens<JsObj>  valueLens = JsObj.optics.lens.value(path);
        JsBigIntLens<JsObj> lens      = JsObj.optics.lens.integralNum(path);
        JsObjLens<JsObj>    lenByKey  = JsObj.optics.lens.obj("a");


        Assertions.assertTrue(lenByKey.exists(it -> it.containsKey("b"))
                                      .test(a));


        Assertions.assertEquals(Optional.of(JsObj.of("b",
                                                     JsObj.of("c",
                                                              JsBigInt.of(BigInteger.TEN)))),
                                lenByKey.find(it -> it.containsKey("b"))
                                      .apply(a)
                               );

        Assertions.assertEquals(BigInteger.TEN,
                                lens.get.apply(a));
        Assertions.assertEquals(JsBigInt.of(BigInteger.TEN),
                                valueLens.get.apply(a));
        Assertions.assertEquals(JsObj.empty()
                                     .set("b",
                                          JsObj.of("c",
                                                   JsBigInt.of(BigInteger.valueOf(10L))
                                                  )
                                         ),
                                lenByKey.get.apply(a));


        JsObj b = lens.set.apply(BigInteger.ONE)
                          .apply(a);

        Assertions.assertEquals(BigInteger.ONE,
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> i.pow(2))
                             .apply(a);

        Assertions.assertEquals(BigInteger.valueOf(100),
                                lens.get.apply(c));


    }


    @Test
    public void testStrLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     "abc"
                                    )
                          );

        JsStrLens<JsObj> lens = JsObj.optics.lens.str(path);

        Assertions.assertEquals("abc",
                                lens.get.apply(a));


        JsObj b = lens.set.apply("abcd")
                          .apply(a);

        Assertions.assertEquals("abcd",
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(String::toUpperCase)
                             .apply(a);

        Assertions.assertEquals("ABC",
                                lens.get.apply(c));


    }


    @Test
    public void testDoubleLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     1.5
                                    )
                          );

        JsDoubleLens<JsObj> lens = JsObj.optics.lens.doubleNum(path);

        Assertions.assertEquals(Double.valueOf(1.5),
                                lens.get.apply(a));


        JsObj b = lens.set.apply(10.5)
                          .apply(a);

        Assertions.assertEquals(Double.valueOf(10.5),
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> i + 1.0)
                             .apply(b);

        Assertions.assertEquals(Double.valueOf(11.5),
                                lens.get.apply(c));


    }

    @Test
    public void testLongLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     Long.MAX_VALUE
                                    )
                          );

        JsLongLens<JsObj> lens = JsObj.optics.lens.longNum(path);

        Assertions.assertEquals(Long.valueOf(Long.MAX_VALUE),
                                lens.get.apply(a));


        JsObj b = lens.set.apply(Long.MIN_VALUE)
                          .apply(a);

        Assertions.assertEquals(Long.valueOf(Long.MIN_VALUE),
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> i + 1)
                             .apply(b);

        Assertions.assertEquals(Long.valueOf(Long.MIN_VALUE + 1),
                                lens.get.apply(c));


    }

    @Test
    public void testIntegerLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     Integer.MAX_VALUE
                                    )
                          );

        JsIntLens<JsObj> lens = JsObj.optics.lens.intNum(path);

        Assertions.assertEquals(Integer.valueOf(Integer.MAX_VALUE),
                                lens.get.apply(a));


        JsObj b = lens.set.apply(Integer.MIN_VALUE)
                          .apply(a);

        Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE),
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> i + 1)
                             .apply(b);

        Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE + 1),
                                lens.get.apply(c));


    }

    @Test
    public void testDecimalLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     JsBigDec.of(new BigDecimal("1.11"))
                                    )
                          );

        JsDecimalLens<JsObj> lens = JsObj.optics.lens.decimalNum(path);

        Assertions.assertEquals(new BigDecimal("1.11"),
                                lens.get.apply(a));


        JsObj b = lens.set.apply(new BigDecimal("10.11"))
                          .apply(a);

        Assertions.assertEquals(new BigDecimal("10.11"),
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> i.plus()
                                          .add(BigDecimal.valueOf(10.0)))
                             .apply(b);

        Assertions.assertEquals(new BigDecimal("20.11"),
                                lens.get.apply(c));


    }


    @Test
    public void testBoolLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     JsBool.TRUE
                                    )
                          );

        JsBoolLens<JsObj> lens = JsObj.optics.lens.bool(path);

        Assertions.assertEquals(true,
                                lens.get.apply(a));


        JsObj b = lens.set.apply(false)
                          .apply(a);

        Assertions.assertEquals(false,
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> !i)
                             .apply(b);

        Assertions.assertEquals(true,
                                lens.get.apply(c));


    }


    @Test
    public void testObjLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     JsObj.empty()
                                    )
                          );

        JsObjLens<JsObj> lens = JsObj.optics.lens.obj(path);

        Assertions.assertEquals(JsObj.empty(),
                                lens.get.apply(a));


        JsObj b = lens.set.apply(JsObj.of("a",
                                          JsInt.of(1)
                                         ))
                          .apply(a);

        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1)
                                        ),
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> i.set("b",
                                               JsStr.of("hi")
                                              ))
                             .apply(b);

        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1)
                                        )
                                     .set("b",
                                          JsStr.of("hi")
                                         ),
                                lens.get.apply(c));


    }

    @Test
    public void testArrayLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     JsArray.empty()
                                    )
                          );

        JsArrayLens<JsObj> lens = JsObj.optics.lens.array(path);

        Assertions.assertEquals(JsArray.empty(),
                                lens.get.apply(a));


        JsObj b = lens.set.apply(JsArray.empty()
                                        .append(JsInt.of(1)))
                          .apply(a);

        Assertions.assertEquals(JsArray.empty()
                                       .append(JsInt.of(1)),
                                lens.get.apply(b));

        JsObj c = lens.modify.apply(i -> i.append(JsInt.of(2)))
                             .apply(b);

        Assertions.assertEquals(JsArray.empty()
                                       .append(JsInt.of(1),
                                               JsInt.of(2)
                                              ),
                                lens.get.apply(c));


    }

    @Test
    public void testStrOptional() {

        Option<JsObj, String> optionalStr = JsObj.optics.optional.str("a");

        Assertions.assertEquals(optionalStr.get.apply(JsObj.empty()),
                                Optional.empty()
                               );
        Assertions.assertEquals(optionalStr.get.apply(JsObj.empty()
                                                           .set("a",
                                                                JsInt.of(1)
                                                               )),
                                Optional.empty()
                               );

        JsObj s = JsObj.empty()
                       .set("a",
                            JsStr.of("a")
                           );
        Assertions.assertEquals(Optional.of("a"),
                                optionalStr.get.apply(s)
                               );

        Assertions.assertEquals(JsStr.of("A"),
                                optionalStr.modify.apply(String::toUpperCase)
                                                  .apply(s)
                                                  .get("a")
                               );
    }


    @Test
    public void testIntOptional() {

        Option<JsObj, Integer> optionalInt = JsObj.optics.optional.intNum("a");

        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()),
                                Optional.empty()
                               );
        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()
                                                           .set("a",
                                                                JsStr.of("1")
                                                               )),
                                Optional.empty()
                               );

        JsObj s = JsObj.empty()
                       .set("a",
                            JsInt.of(1)
                           );
        Assertions.assertEquals(Optional.of(1),
                                optionalInt.get.apply(s)
                               );

        Assertions.assertEquals(JsInt.of(2),
                                optionalInt.modify.apply(i -> i + 1)
                                                  .apply(s)
                                                  .get("a")
                               );
    }


    @Test
    public void testLongOptional() {

        Option<JsObj, Long> optionalInt = JsObj.optics.optional.longNum("a");

        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()),
                                Optional.empty()
                               );
        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()
                                                           .set("a",
                                                                JsStr.of("1")
                                                               )),
                                Optional.empty()
                               );

        JsObj s = JsObj.empty()
                       .set("a",
                            JsLong.of(Long.MAX_VALUE)
                           );
        Assertions.assertEquals(Optional.of(Long.MAX_VALUE),
                                optionalInt.get.apply(s)
                               );

        Assertions.assertEquals(JsLong.of(Long.MAX_VALUE - 1),
                                optionalInt.modify.apply(i -> i - 1)
                                                  .apply(s)
                                                  .get("a")
                               );
    }


    @Test
    public void testDoubleOptional() {

        Option<JsObj, Double> optionalInt = JsObj.optics.optional.doubleNum("a");

        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()),
                                Optional.empty()
                               );
        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()
                                                           .set("a",
                                                                JsStr.of("1")
                                                               )),
                                Optional.empty()
                               );

        JsObj s = JsObj.empty()
                       .set("a",
                            JsDouble.of(1.5)
                           );
        Assertions.assertEquals(Optional.of(1.5),
                                optionalInt.get.apply(s)
                               );

        Assertions.assertEquals(JsDouble.of(2.5),
                                optionalInt.modify.apply(i -> i + 1)
                                                  .apply(s)
                                                  .get("a")
                               );
    }


    @Test
    public void testDecimalOptional() {

        Option<JsObj, BigDecimal> optionalInt = JsObj.optics.optional.decimalNum("a");

        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()),
                                Optional.empty()
                               );
        Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()
                                                           .set("a",
                                                                JsStr.of("1")
                                                               )),
                                Optional.empty()
                               );

        JsObj s = JsObj.empty()
                       .set("a",
                            JsBigDec.of(BigDecimal.valueOf(1.5))
                           );
        Assertions.assertEquals(Optional.of(BigDecimal.valueOf(1.5)),
                                optionalInt.get.apply(s)
                               );

        Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(2.5)),
                                optionalInt.modify.apply(i -> i.plus()
                                                               .add(BigDecimal.valueOf(1)))
                                                  .apply(s)
                                                  .get("a")
                               );
    }
}
