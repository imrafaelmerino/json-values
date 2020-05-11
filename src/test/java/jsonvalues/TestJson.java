package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class TestJson
{

  @Test
  public void testAppendStringsCreateArrayInObj()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                                   "a",
                                   "b"
                                  );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendStringsCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                                     "a",
                                     "b"
                                    );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendStringsOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                         .put(JsPath.path("/a/b"),
                              1
                             );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                                   "a",
                                   "b"
                                  );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendStringsOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                             .put(JsPath.path("/a/b"),
                                  1
                                 );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                                     "a",
                                     "b"
                                    );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendValuesCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                                   JsStr.of("a"),
                                   JsInt.of(1)
                                  );
    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendValuesCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                                     JsStr.of("a"),
                                     JsInt.of(1)
                                    );
    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendValuesOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                         .put(JsPath.path("/a/b"),
                              1
                             );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                                   JsStr.of("a"),
                                   JsInt.of(1)
                                  );

    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendValuesOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                             .put(JsPath.path("/a/b"),
                                  1
                                 );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                                     JsStr.of("a"),
                                     JsInt.of(1)
                                    );

    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendIntCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                                   1,
                                   2
                                  );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendIntCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                                     1,
                                     2
                                    );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendIntOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                         .put(JsPath.path("/a/b"),
                              1
                             );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                                   1,
                                   2
                                  );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendIntOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                             .put(JsPath.path("/a/b"),
                                  1
                                 );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                                     1,
                                     2
                                    );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                                   1L,
                                   2L
                                  );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                                     1L,
                                     2L
                                    );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                         .put(JsPath.path("/a/b"),
                              1
                             );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                                   1L,
                                   2L
                                  );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                             .put(JsPath.path("/a/b"),
                                  1
                                 );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                                     1L,
                                     2L
                                    );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendBoolCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                                   true,
                                   false
                                  );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendBoolCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                                     true,
                                     false
                                    );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendBoolOverwriteAnyElement()
  {

    Json<JsObj> a = JsObj.empty()
                         .put(JsPath.path("/a/b"),
                              1
                             );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                                   true,
                                   false
                                  );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendBoolOverwriteAnyElementInArray()
  {

    final JsPath path = JsPath.path("/0/b");
    Json<JsArray> a = JsArray.empty()
                             .put(path,
                                  1
                                 );

    final Json<JsArray> b = a.append(path,
                                     true,
                                     false
                                    );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleCreateArrayInObj()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                                   1.5,
                                   1.6
                                  );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                                     1.5,
                                     1.6
                                    );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleOverwriteAnyElementInObject()
  {

    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> a = JsObj.empty()
                         .put(path,
                              1
                             );

    final Json<JsObj> b = a.append(path,
                                   1.5,
                                   1.6
                                  );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleOverwriteAnyElementInArray()
  {

    final JsPath path = JsPath.path("/0/b");
    Json<JsArray> a = JsArray.empty()
                             .put(path,
                                  1
                                 );

    final Json<JsArray> b = a.append(path,
                                     1.5,
                                     1.6
                                    );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void appendAllIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                          JsArray.empty()
                                         ));

    Json<JsObj> a = json.appendAllIfPresent(path,
                                            () -> JsArray.of(1,
                                                             2,
                                                             3
                                                            )
                                           );

    Assertions.assertEquals(JsArray.of(1,
                                       2,
                                       3
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                         .appendAllIfPresent(path,
                                             () -> JsArray.of(1,
                                                              2,
                                                              3
                                                             )
                                            );

    Assertions.assertNull(
                            b.getArray(path)
                           );

  }

  @Test
  public void appendIfPresentWithSupplier()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                          JsArray.empty()
                                         ));

    Json<JsObj> a = json.appendIfPresent(path,
                                         () -> JsInt.of(1)
                                        );

    Assertions.assertTrue(1 == a.getInt(path.index(0)));

    Json<JsObj> b = JsObj.empty()
                         .appendIfPresent(path,
                                          () -> JsInt.of(1)
                                         );

    Assertions.assertNull(
                            b.getArray(path)
                           );

  }


  @Test
  public void appendArrayOfStringsIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                          JsArray.empty()
                                         ));

    Json<JsObj> a = json.appendIfPresent(path,
                                         "a",
                                         "b"
                                        );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                         .appendIfPresent(path,
                                          "a",
                                          "b"
                                         );

    Assertions.assertNull(
                            b.getArray(path)
                           );

  }


  @Test
  public void appendArrayOfIntegersIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                          JsArray.empty()
                                         ));

    Json<JsObj> a = json.appendIfPresent(path,
                                         1,
                                         2
                                        );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                         .appendIfPresent(path,
                                          1,
                                          2
                                         );

    Assertions.assertNull(
                            b.getArray(path)
                           );

  }

  @Test
  public void appendArrayOfLongsIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                          JsArray.empty()
                                         ));

    Json<JsObj> a = json.appendIfPresent(path,
                                         1L,
                                         2L
                                        );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                         .appendIfPresent(path,
                                          1L,
                                          2L
                                         );

    Assertions.assertNull(
                            b.getArray(path)
                           );

  }

  @Test
  public void appendArrayOfDoubleIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                          JsArray.empty()
                                         ));

    Json<JsObj> a = json.appendIfPresent(path,
                                         1.5,
                                         2.5
                                        );

    Assertions.assertEquals(JsArray.of(1.5,
                                       2.5
                                      ),
                            a.getArray(path)
                           );

    Json b = JsObj.empty()
                  .appendIfPresent(path,
                                   1.5,
                                   2.5
                                  );

    Assertions.assertNull(
                            b.getArray(path)
                           );

  }


  @Test
  public void appendArrayOfBoolIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                          JsArray.empty()
                                         ));

    Json<JsObj> a = json.appendIfPresent(path,
                                         true,
                                         false
                                        );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                         .appendIfPresent(path,
                                          true,
                                          false
                                         );

    Assertions.assertNull(
                            b.getArray(path)
                           );

  }


  @Test
  public void testGetMethods()
  {

    Json<JsObj> a = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsStr.of("hi"),
                             "c",
                             JsBool.TRUE,
                             "d",
                             JsLong.of(1),
                             "e",
                             JsDouble.of(1.5),
                             "f",
                             JsBigInt.of(BigInteger.TEN),
                             "g",
                             JsBigDec.of(BigDecimal.TEN),
                             "h",
                             JsArray.of(JsStr.of("bye"),
                                        JsInt.of(1),
                                        JsBool.FALSE,
                                        JsLong.of(1L)
                                       ),
                             "i",
                             JsObj.of("a",
                                      JsInt.of(1),
                                      "b",
                                      JsArray.of(1,
                                                 2,
                                                 3
                                                ),
                                      "c",
                                      JsObj.empty()
                                     )
                            );

    Assertions.assertTrue(1 == a.getInt(JsPath.path("/a")));
    Assertions.assertEquals("hi",
                            a.getStr(JsPath.path("/b"))
                           );
    Assertions.assertEquals(true,
                            a.getBool(JsPath.path("/c"))
                           );
    Assertions.assertTrue(1L == a.getLong(JsPath.path("/d")));
    Assertions.assertTrue(1.5 == a.getDouble(JsPath.path("/e")));
    Assertions.assertEquals(BigInteger.TEN,
                            a.getBigInt(JsPath.path("/f"))
                           );
    Assertions.assertEquals(BigDecimal.TEN,
                            a.getBigDec(JsPath.path("/g"))
                           );
    Assertions.assertEquals(JsArray.of(1,
                                       2,
                                       3
                                      ),
                            a.getArray(JsPath.path("/i/b"))
                           );

    Assertions.assertEquals(JsObj.empty(),
                            a.getObj(JsPath.path("/i/c"))
                           );

    Assertions.assertNull(a.getInt(JsPath.path("/b")));
    Assertions.assertNull(a.getLong(JsPath.path("/b")));
    Assertions.assertNull(a.getBigDec(JsPath.path("/b")));
    Assertions.assertNull(a.getBigInt(JsPath.path("/b")));
    Assertions.assertNull(a.getBool(JsPath.path("/b")));
    Assertions.assertNull(a.getObj(JsPath.path("/b")));
    Assertions.assertNull(a.getArray(JsPath.path("/b")));

    Assertions.assertNull(
                            a.getInt(JsPath.path("/b"))
                           );
    Assertions.assertEquals(null,
                            a.getLong(JsPath.path("/b"))
                           );
    Assertions.assertNull(
                            a.getBigDec(JsPath.path("/b"))
                           );
    Assertions.assertNull(
                            a.getBigInt(JsPath.path("/b"))
                           );
    Assertions.assertNull(
                            a.getBool(JsPath.path("/b"))
                           );
    Assertions.assertNull(
                            a.getObj(JsPath.path("/b"))
                           );
    Assertions.assertNull(
                            a.getArray(JsPath.path("/b"))
                           );

  }

  @Test
  public void testPutIfAbsent()
  {

    Json<?> json = JsObj.empty();

    JsPath path = JsPath.path("/a/b/c/0");

    Json<?> a = json.putIfAbsent(path,
                                 1
                                );

    Assertions.assertTrue(1 == a.getInt(path));
    Assertions.assertTrue(1 == a.putIfAbsent(path,
                                             10
                                            )
                                .getInt(path));

    Json<?> b = json.putIfAbsent(path,
                                 "a"
                                );

    Assertions.assertEquals("a",
                            b.getStr(path)
                           );
    Assertions.assertEquals("a",
                            b.putIfAbsent(path,
                                          "hi"
                                         )
                             .getStr(path)
                           );


    Json<?> c = json.putIfAbsent(path,
                                 true
                                );

    Assertions.assertEquals(true,
                            c.getBool(path)
                           );
    Assertions.assertEquals(true,
                            c.putIfAbsent(path,
                                          false
                                         )
                             .getBool(path)
                           );

    Json<?> d = json.putIfAbsent(path,
                                 1L
                                );

    Assertions.assertTrue(1L == d.getLong(path));
    Assertions.assertTrue(1L == d.putIfAbsent(path,
                                              10L
                                             )
                                 .getLong(path));

    Json<?> e = json.putIfAbsent(path,
                                 JsObj.empty()
                                );

    Assertions.assertSame(JsObj.empty(),
                          e.getObj(path)
                         );
    Assertions.assertSame(JsObj.empty(),
                          e.putIfAbsent(path,
                                        JsObj.of("a",
                                                 JsInt.of(1)
                                                )
                                       )
                           .getObj(path)
                         );
    Json<?> f = json.putIfAbsent(path,
                                 JsObj.empty()
                                );

    Assertions.assertSame(JsObj.empty(),
                          f.getObj(path)
                         );

    Json<?> g = json.putIfAbsent(path,
                                 JsArray.empty()
                                );

    Assertions.assertSame(JsArray.empty(),
                          g.getArray(path)
                         );


    Json<?> h = json.putIfAbsent(path,
                                 new BigDecimal("1.4")
                                );

    Assertions.assertEquals(new BigDecimal("1.4"),
                            h.getBigDec(path)
                           );


    Json<?> i = json.putIfAbsent(path,
                                 JsStr.of("hi")
                                );

    Assertions.assertEquals("hi",
                            i.getStr(path)
                           );


    Json<?> j = json.putIfAbsent(path,
                                 BigInteger.TEN
                                );

    Assertions.assertEquals(BigInteger.TEN,
                            j.getBigInt(path)
                           );
    Assertions.assertEquals(BigInteger.TEN,
                            j.putIfAbsent(path,
                                          BigInteger.ONE
                                         )
                             .getBigInt(path)
                           );

    Json<?> k = json.putIfAbsent(path,
                                 1.5
                                );

    Assertions.assertTrue(1.5 ==
                            k.getDouble(path)
                         );
    Assertions.assertTrue(1.5 ==
                            k.putIfAbsent(path,
                                          10.5
                                         )
                             .getDouble(path)
                         );


    Json<?> l = json.putIfAbsent(path,
                                 () -> JsDouble.of(1.5)
                                );

    Assertions.assertTrue(1.5 ==
                            l.getDouble(path)
                         );
    Assertions.assertTrue(1.5 ==
                            l.putIfAbsent(path,
                                          () -> JsDouble.of(11.5)
                                         )
                             .getDouble(path)
                         );

  }


  @Test
  public void testPrepend()
  {

    final JsPath path = JsPath.path("/a/b/c");
    Json<?> json = JsObj.empty()
                        .put(path,
                             JsArray.empty()
                            );

    final Json<?> a = json.prepend(path,
                                   1,
                                   2
                                  )
                          .prepend(path,
                                   "a",
                                   "b"
                                  )
                          .prepend(path,
                                   true,
                                   false
                                  )
                          .prepend(path,
                                   1L,
                                   2L
                                  )
                          .prepend(path,
                                   1.3,
                                   1.4
                                  )
                          .prepend(path,
                                   JsStr.of("a"),
                                   JsInt.of(10)
                                  );


    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(10),
                                       JsDouble.of(1.3),
                                       JsDouble.of(1.4),
                                       JsLong.of(1L),
                                       JsLong.of(2L),
                                       JsBool.TRUE,
                                       JsBool.FALSE,
                                       JsStr.of("a"),
                                       JsStr.of("b"),
                                       JsInt.of(1),
                                       JsInt.of(2)
                                      ),
                            a.getArray(path)
                           );
  }


  @Test
  public void testPutIfPresent()
  {
    JsPath path = JsPath.path("/0/a/b");
    Json<?> empty = JsArray.empty();
    Json<?> a = JsArray.empty()
                       .put(path,
                            JsNull.NULL
                           );

    Assertions.assertNull(empty.putIfPresent(path,
                                             "a"
                                            )
                               .getStr(path));
    Assertions.assertEquals("a",
                            a.putIfPresent(path,
                                           "a"
                                          )
                             .getStr(path)
                           );

    Assertions.assertNull(empty.putIfPresent(path,
                                             1
                                            )
                               .getInt(path));
    Assertions.assertTrue(1 == a.putIfPresent(path,
                                              1
                                             )
                                .getInt(path));

    Assertions.assertNull(empty.putIfPresent(path,
                                             1L
                                            )
                               .getLong(path));
    Assertions.assertTrue(1 == a.putIfPresent(path,
                                              1L
                                             )
                                .getLong(path));

    Assertions.assertNull(empty.putIfPresent(path,
                                             true
                                            )
                               .getBool(path));
    Assertions.assertEquals(true,
                            a.putIfPresent(path,
                                           true
                                          )
                             .getBool(path)
                           );

    Assertions.assertNull(empty.putIfPresent(path,
                                             BigInteger.TEN
                                            )
                               .getBigInt(path));
    Assertions.assertEquals(BigInteger.TEN,
                            a.putIfPresent(path,
                                           BigInteger.TEN
                                          )
                             .getBigInt(path)
                           );

    Assertions.assertNull(empty.putIfPresent(path,
                                             BigDecimal.TEN
                                            )
                               .getBigDec(path));
    Assertions.assertEquals(BigDecimal.TEN,
                            a.putIfPresent(path,
                                           BigDecimal.TEN
                                          )
                             .getBigDec(path)
                           );

    Assertions.assertNull(empty.putIfPresent(path,
                                             JsObj.empty()
                                            )
                               .getObj(path));
    Assertions.assertEquals(JsObj.empty(),
                            a.putIfPresent(path,
                                           JsObj.empty()
                                          )
                             .getObj(path)
                           );

    Assertions.assertNull(empty.putIfPresent(path,
                                             JsArray.empty()
                                            )
                               .getArray(path));
    Assertions.assertEquals(JsArray.empty(),
                            a.putIfPresent(path,
                                           JsArray.empty()
                                          )
                             .getArray(path)
                           );

    Assertions.assertNull(empty.putIfPresent(path,
                                             JsStr.of("a")
                                            )
                               .getStr(path));
    Assertions.assertEquals("a",
                            a.putIfPresent(path,
                                           JsStr.of("a")
                                          )
                             .getStr(path)
                           );
  }

  @Test
  public void test_times()
  {

    final JsObj a = JsObj.of("a",
                             JsArray.of(JsObj.of("a",
                                                 JsInt.of(1)
                                                ),
                                        JsNull.NULL,
                                        JsInt.of(1)
                                       ),
                             "b",
                             JsInt.of(1)
                            );

    Assertions.assertTrue(1 == a.times(JsInt.of(1)));
    Assertions.assertTrue(3 == a.timesAll(JsInt.of(1)));
    Assertions.assertTrue(2 == a.size());

    final OptionalInt size = a.size(JsPath.path("/a"));
    System.out.println(size);
    Assertions.assertEquals(OptionalInt.of(3),
                            size
                           );
  }


  @Test
  public void prepend_if_present()
  {
    final JsPath path = JsPath.empty()
                              .key("a")
                              .key("b");
    final JsObj a = JsObj.empty()
                         .put(path,
                              JsArray.empty()
                             )
                         .prependIfPresent(path,
                                           1,
                                           2
                                          );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            a.getArray(path)
                           );

    Assertions.assertEquals(JsObj.empty(),
                            JsObj.empty()
                                 .prependIfPresent(path,
                                                   "a"
                                                  )
                           );

    final JsObj b = a.prependIfPresent(path,
                                       "a",
                                       "b"
                                      );

    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsStr.of("b"),
                                       JsInt.of(1),
                                       JsInt.of(2)
                                      ),
                            b.getArray(path)
                           );


    final JsObj c = b.prependIfPresent(path,
                                       true,
                                       false
                                      );


    Assertions.assertEquals(JsArray.of(JsBool.TRUE,
                                       JsBool.FALSE,
                                       JsStr.of("a"),
                                       JsStr.of("b"),
                                       JsInt.of(1),
                                       JsInt.of(2)
                                      ),
                            c.getArray(path)
                           );


    final JsObj d = c.prependIfPresent(path,
                                       10L,
                                       20L
                                      );


    Assertions.assertEquals(JsArray.of(JsLong.of(10L),
                                       JsLong.of(20L),
                                       JsBool.TRUE,
                                       JsBool.FALSE,
                                       JsStr.of("a"),
                                       JsStr.of("b"),
                                       JsInt.of(1),
                                       JsInt.of(2)
                                      ),
                            d.getArray(path)
                           );


    final JsObj e = d.prependIfPresent(path,
                                       10.5,
                                       12.5
                                      );


    Assertions.assertEquals(JsArray.of(JsDouble.of(10.5),
                                       JsDouble.of(12.5),
                                       JsLong.of(10L),
                                       JsLong.of(20L),
                                       JsBool.TRUE,
                                       JsBool.FALSE,
                                       JsStr.of("a"),
                                       JsStr.of("b"),
                                       JsInt.of(1),
                                       JsInt.of(2)
                                      ),
                            e.getArray(path)
                           );


    Assertions.assertEquals(JsObj.empty(),
                            JsObj.empty()
                                 .prependIfPresent(path,
                                                   1,
                                                   2
                                                  )
                                 .prependIfPresent(path,
                                                   "a",
                                                   "b"
                                                  )
                                 .prependIfPresent(path,
                                                   1L,
                                                   2L
                                                  )
                                 .prependIfPresent(path,
                                                   true,
                                                   false
                                                  )
                                 .prependIfPresent(path,
                                                   1.5,
                                                   2.5
                                                  )
                           );




  }


  @Test
  public void test_prepend_if_all_suppliers(){
    JsPath path = JsPath.fromKey("a").key("b");
    JsObj a = JsObj.empty().put(path,JsArray.of(3));

    final JsObj b = a.prependAllIfPresent(path,
                                         () -> JsArray.of(0,
                                                          1,
                                                          2));

    Assertions.assertEquals(JsArray.of(0,1,2,3),b.getArray(path));

    final JsObj d = b.prependIfPresent(path,
                                         () -> JsInt.of(-1));

    Assertions.assertEquals(JsArray.of(-1,0,1,2,3),d.getArray(path));

  }


}
