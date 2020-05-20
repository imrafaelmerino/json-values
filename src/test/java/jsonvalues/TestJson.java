package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestJson {


    @Test
    public void testGetMethods() {

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
    public void test_times() {

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

        Assertions.assertEquals(1,
                                a.times(JsInt.of(1))
                               );
        Assertions.assertEquals(3,
                                a.timesAll(JsInt.of(1))
                               );
        Assertions.assertEquals(2,
                                a.size()
                               );


    }


}
