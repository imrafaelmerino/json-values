package jsonvalues;

import io.vavr.Tuple2;
import jsonvalues.supplier.JsArraySupplier;
import jsonvalues.supplier.JsObjSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestSupplier {


    @Test
    public void testTwoSupplier() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE

                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE
                                        ),
                                supplier.get());


    }
    @Test
    public void testSupplierThree() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL)

                                        ),
                                supplier.get());


    }

    @Test
    public void testSupplierFour() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE)

                                        ),
                                supplier.get());


    }

    @Test
    public void testSupplierFive() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE
                                        ),
                                supplier.get());


    }


    @Test
    public void testSupplierSix() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE
                                        ),
                                supplier.get());


    }

    @Test
    public void testSupplierSeven() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL
                                        ),
                                supplier.get());


    }

    @Test
    public void testSupplierEight() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a")
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a")
                                        ),
                                supplier.get());


    }


    @Test
    public void testSupplierNine() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a"),
                                                  "j",()-> JsInt.of(1)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a"),
                                         "j", JsInt.of(1)
                                        ),
                                supplier.get());


    }


    @Test
    public void testSupplierTen() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a"),
                                                  "j",()-> JsInt.of(1),
                                                  "k",()-> JsLong.of(1L)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a"),
                                         "j", JsInt.of(1),
                                         "k",JsLong.of(1L)
                                        ),
                                supplier.get());


    }


    @Test
    public void testSupplierEleven() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a"),
                                                  "j",()-> JsInt.of(1),
                                                  "k",()-> JsLong.of(1L),
                                                  "l",()-> JsDouble.of(1.5)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a"),
                                         "j", JsInt.of(1),
                                         "k",JsLong.of(1L),
                                         "l", JsDouble.of(1.5)
                                        ),
                                supplier.get());


    }


    @Test
    public void testSupplierTwelve() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a"),
                                                  "j",()-> JsInt.of(1),
                                                  "k",()-> JsLong.of(1L),
                                                  "l",()-> JsDouble.of(1.5),
                                                  "m",()->JsBigInt.of(BigInteger.TEN)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a"),
                                         "j", JsInt.of(1),
                                         "k",JsLong.of(1L),
                                         "l", JsDouble.of(1.5),
                                         "m",JsBigInt.of(BigInteger.TEN)
                                        ),
                                supplier.get());


    }

    @Test
    public void testSupplierThirteen() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a"),
                                                  "j",()-> JsInt.of(1),
                                                  "k",()-> JsLong.of(1L),
                                                  "l",()-> JsDouble.of(1.5),
                                                  "m",()->JsBigInt.of(BigInteger.TEN),
                                                  "n",()->JsBigDec.of(BigDecimal.TEN)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a"),
                                         "j", JsInt.of(1),
                                         "k",JsLong.of(1L),
                                         "l", JsDouble.of(1.5),
                                         "m",JsBigInt.of(BigInteger.TEN),
                                         "n",JsBigDec.of(BigDecimal.TEN)
                                        ),
                                supplier.get());


    }

    @Test
    public void testSupplierFourteen() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a"),
                                                  "j",()-> JsInt.of(1),
                                                  "k",()-> JsLong.of(1L),
                                                  "l",()-> JsDouble.of(1.5),
                                                  "m",()->JsBigInt.of(BigInteger.TEN),
                                                  "n",()->JsBigDec.of(BigDecimal.TEN),
                                                  "o",JsArraySupplier.tuple(()->JsBool.FALSE, ()->JsNull.NULL)
                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a"),
                                         "j", JsInt.of(1),
                                         "k",JsLong.of(1L),
                                         "l", JsDouble.of(1.5),
                                         "m",JsBigInt.of(BigInteger.TEN),
                                         "n",JsBigDec.of(BigDecimal.TEN),
                                         "o",JsArray.of(JsBool.FALSE,JsNull.NULL)
                                        ),
                                supplier.get());


    }

    @Test
    public void testSupplierFifteen() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.tuple(()-> JsDouble.of(1.5), ()->JsBool.FALSE),
                                                  "f",()->JsBool.TRUE,
                                                  "g",()->JsBool.FALSE,
                                                  "h",()->JsNull.NULL,
                                                  "i",()-> JsStr.of("a"),
                                                  "j",()-> JsInt.of(1),
                                                  "k",()-> JsLong.of(1L),
                                                  "l",()-> JsDouble.of(1.5),
                                                  "m",()->JsBigInt.of(BigInteger.TEN),
                                                  "n",()->JsBigDec.of(BigDecimal.TEN),
                                                  "o",JsArraySupplier.tuple(()->JsBool.FALSE, ()->JsNull.NULL),
                                                  "p",()->JsNull.NULL

                                                 );


        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE,
                                         "c",JsObj.of("d",JsNull.NULL),
                                         "e",JsArray.of(JsDouble.of(1.5),JsBool.FALSE),
                                         "f",JsBool.TRUE,
                                         "g",JsBool.FALSE,
                                         "h",JsNull.NULL,
                                         "i", JsStr.of("a"),
                                         "j", JsInt.of(1),
                                         "k",JsLong.of(1L),
                                         "l", JsDouble.of(1.5),
                                         "m",JsBigInt.of(BigInteger.TEN),
                                         "n",JsBigDec.of(BigDecimal.TEN),
                                         "o",JsArray.of(JsBool.FALSE,JsNull.NULL),
                                         "p",JsNull.NULL
                                        ),
                                supplier.get());


    }


    @Test
    public void testPutSupplier(){

        JsObjSupplier supplier = JsObjSupplier.empty()
                                       .set("a",
                                            () -> JsInt.of(1));

        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1)), supplier.get());
    }

    @Test
    public void testAppend(){

        JsArraySupplier a = JsArraySupplier.empty()
                                           .append(() -> JsNull.NULL)
                                           .append(() -> JsStr.of("a"));


        Assertions.assertEquals(JsArray.of(JsNull.NULL,
                                           JsStr.of("a")), a.get());
    }
}
