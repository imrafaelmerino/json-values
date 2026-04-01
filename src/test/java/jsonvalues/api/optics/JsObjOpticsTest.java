package jsonvalues.api.optics;


import fun.optic.Lens;
import fun.optic.Option;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBinary;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNothing;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.UserError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsObjOpticsTest {

  @Test
  public void shouldBigDecLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsBigInt.of(BigInteger.TEN)

                      );

    Lens<JsObj, JsValue> valueLens = JsObj.lens.value(path);
    Lens<JsObj, BigInteger> lens = JsObj.lens.integralNum(path);
    Lens<JsObj, JsObj> lenByKey = JsObj.lens.obj("a");

    Assertions.assertTrue(lenByKey.exists.apply(it -> it.containsKey("b"))
                                         .test(a));

    Assertions.assertEquals(Optional.of(JsObj.of("b",
                                                 JsObj.of("c",
                                                          JsBigInt.of(BigInteger.TEN)))),
                            lenByKey.find.apply(it -> it.containsKey("b"))
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
  public void shouldStrLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsStr.of("abc")

                      );

    Lens<JsObj, String> lens = JsObj.lens.str(path);

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
  public void shouldDoubleLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsDouble.of(1.5)

                      );

    Lens<JsObj, Double> lens = JsObj.lens.doubleNum(path);

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
  public void shouldLongLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsLong.of(Long.MAX_VALUE)

                      );

    Lens<JsObj, Long> lens = JsObj.lens.longNum(path);

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
  public void shouldIntegerLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsInt.of(Integer.MAX_VALUE)

                      );

    Lens<JsObj, Integer> lens = JsObj.lens.intNum(path);

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
  public void shouldDecimalLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsBigDec.of(new BigDecimal("1.11"))

                      );

    Lens<JsObj, BigDecimal> lens = JsObj.lens.decimalNum(path);

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
  public void shouldBoolLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsBool.TRUE

                      );

    Lens<JsObj, Boolean> lens = JsObj.lens.bool(path);

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
  public void shouldObjLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsObj.empty()

                      );

    Lens<JsObj, JsObj> lens = JsObj.lens.obj(path);

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
  public void shouldArrayLenses() {

    JsPath path = JsPath.path("/a/b/c");
    JsObj a = JsObj.of(path,
                       JsArray.empty()

                      );

    Lens<JsObj, JsArray> lens = JsObj.lens.array(path);

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
  public void shouldStrOptional() {

    Option<JsObj, String> optionalStr = JsObj.optional.str("a");

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
  public void shouldIntOptional() {

    Option<JsObj, Integer> optionalInt = JsObj.optional.intNum("a");

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
  public void shouldLongOptional() {

    Option<JsObj, Long> optionalInt = JsObj.optional.longNum("a");

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
  public void shouldDoubleOptional() {

    Option<JsObj, Double> optionalInt =
        JsObj.optional.doubleNum("a");

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
  public void shouldDecimalOptional() {

    Option<JsObj, BigDecimal> optionalInt = JsObj.optional.decimalNum("a");

    Option<JsObj, BigDecimal> optionalDec = JsObj.optional.decimalNum(JsPath.path("/b/c"));

    Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()),
                            Optional.empty()
                           );
    Assertions.assertEquals(optionalDec.get.apply(JsObj.empty()),
                            Optional.empty()
                           );
    Assertions.assertEquals(optionalInt.get.apply(JsObj.empty()
                                                       .set("a",
                                                            JsStr.of("1")
                                                           )),
                            Optional.empty()
                           );

    Assertions.assertEquals(optionalDec.get.apply(JsObj.empty()
                                                       .set("b",
                                                            JsObj.of("c",
                                                                     JsStr.of("hi"))
                                                           )),
                            Optional.empty()
                           );

    JsObj s = JsObj.empty()
                   .set("a",
                        JsBigDec.of(BigDecimal.valueOf(1.5))
                       )
                   .set("b",
                        JsObj.of("c",
                                 JsBigDec.of(BigDecimal.valueOf(0.5))));
    Assertions.assertEquals(Optional.of(BigDecimal.valueOf(1.5)),
                            optionalInt.get.apply(s)
                           );
    Assertions.assertEquals(Optional.of(BigDecimal.valueOf(0.5)),
                            optionalDec.get.apply(s)
                           );

    Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(2.5)),
                            optionalInt.modify.apply(i -> i.plus()
                                                           .add(BigDecimal.valueOf(1)))
                                              .apply(s)
                                              .get("a")
                           );
  }

  @Test
  public void shouldValueLenses() {

    Lens<JsObj, JsValue> b = JsObj.lens.value(JsPath.path("/a/b"));
    Lens<JsObj, JsValue> head = JsObj.lens.value(JsPath.path("/a/c/0"));

    JsObj obj = JsObj.of("a",
                         JsObj.of("b",
                                  JsBool.TRUE,
                                  "c",
                                  JsArray.of(JsNull.NULL)
                                 )
                        );
    Assertions.assertEquals(JsBool.TRUE,
                            b.get.apply(obj));
    Assertions.assertEquals(JsNull.NULL,
                            head.get.apply(obj));

    Assertions.assertEquals(JsNothing.NOTHING,
                            b.get.apply(JsObj.empty()));
    Assertions.assertEquals(JsNothing.NOTHING,
                            head.get.apply(JsObj.empty()));

  }

  @Test
  public void shouldObjArrayBinaryInstantOptionalsByPathAndKey() {

    JsPath objPath = JsPath.path("/meta/obj");
    JsPath arrPath = JsPath.path("/meta/arr");
    JsPath instantPath = JsPath.path("/meta/time");
    JsPath binaryPath = JsPath.path("/meta/blob");
    Instant now = Instant.parse("2021-05-10T10:15:30Z");
    byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);

    JsObj obj = JsObj.empty()
                     .set(objPath,
                          JsObj.of("a",
                                   JsInt.of(1)))
                     .set(arrPath,
                          JsArray.of(1,
                                     2))
                     .set(instantPath,
                          JsInstant.of(now))
                     .set(binaryPath,
                          JsBinary.of(bytes));

    Assertions.assertEquals(Optional.of(JsObj.of("a",
                                                 JsInt.of(1))),
                            JsObj.optional.obj(objPath)
                                          .get.apply(obj));
    Assertions.assertEquals(Optional.of(JsArray.of(1,
                                                   2)),
                            JsObj.optional.array(arrPath)
                                          .get.apply(obj));
    Assertions.assertEquals(Optional.of(now),
                            JsObj.optional.instant(instantPath)
                                          .get.apply(obj));
    Assertions.assertArrayEquals(bytes,
                                 JsObj.optional.binary(binaryPath)
                                               .get.apply(obj)
                                               .orElseThrow());

    Assertions.assertEquals(Optional.of(JsObj.of("a",
                                                 JsInt.of(1))),
                            JsObj.optional.obj("meta")
                                          .get.apply(obj)
                                          .flatMap(it -> JsObj.optional.obj("obj")
                                                                 .get.apply(it)));
  }

  @Test
  public void shouldObjOptionalsWithWrongTypeAndMissingValues() {

    JsObj obj = JsObj.of("a",
                         JsStr.of("not-an-object"));

    Assertions.assertEquals(Optional.empty(),
                            JsObj.optional.obj("a")
                                          .get.apply(obj));
    Assertions.assertEquals(Optional.empty(),
                            JsObj.optional.array("a")
                                          .get.apply(obj));
    Assertions.assertEquals(Optional.empty(),
                            JsObj.optional.binary("a")
                                          .get.apply(obj));
    Assertions.assertEquals(Optional.empty(),
                            JsObj.optional.instant("a")
                                          .get.apply(obj));
    Assertions.assertEquals(Optional.empty(),
                            JsObj.optional.str("missing")
                                          .get.apply(obj));
  }

  @Test
  public void shouldObjPathValidationForLensAndOptional() {

    JsPath indexHeadPath = JsPath.path("/0");

    Assertions.assertThrows(UserError.class,
                            () -> JsObj.lens.str(indexHeadPath));
    Assertions.assertThrows(UserError.class,
                            () -> JsObj.optional.str(indexHeadPath));
    Assertions.assertThrows(UserError.class,
                            () -> JsObj.optional.obj(indexHeadPath));
    Assertions.assertThrows(UserError.class,
                            () -> JsObj.optional.array(indexHeadPath));
  }
}
