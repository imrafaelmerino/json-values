package jsonvalues;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsOpticsTest {

   @Test
    public void testBigDecLenses() {

       JsPath path = JsPath.path("/a/b/c");
       JsObj a = JsObj.of(JsPair.of(path,
                                    BigInteger.TEN
                                   )
                         );

       JsBigIntLens<JsObj> lens = JsObj.optics.lens.integralNum(path);

       Assertions.assertEquals( BigInteger.TEN,lens.get.apply(a));


       JsObj b = lens.set.apply(BigInteger.ONE)
                             .apply(a);

       Assertions.assertEquals( BigInteger.ONE,lens.get.apply(b));

       JsObj c = lens.modify.apply(i -> i.pow(2))
                                .apply(a);

       Assertions.assertEquals( BigInteger.valueOf(100),lens.get.apply(c));



   }


    @Test
    public void testStrLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     "abc"
                                    )
                          );

        JsStrLens<JsObj> lens = JsObj.optics.lens.str(path);

        Assertions.assertEquals( "abc",lens.get.apply(a));


        JsObj b = lens.set.apply("abcd")
                          .apply(a);

        Assertions.assertEquals( "abcd",lens.get.apply(b));

        JsObj c = lens.modify.apply(String::toUpperCase)
                             .apply(a);

        Assertions.assertEquals( "ABC",lens.get.apply(c));



    }


    @Test
    public void testDoubleLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     1.5
                                    )
                          );

        JsDoubleLens<JsObj> lens = JsObj.optics.lens.doubleNum(path);

        Assertions.assertEquals( Double.valueOf(1.5),lens.get.apply(a));


        JsObj b = lens.set.apply(10.5)
                          .apply(a);

        Assertions.assertEquals( Double.valueOf(10.5),lens.get.apply(b));

        JsObj c = lens.modify.apply(i->i+1.0)
                             .apply(b);

        Assertions.assertEquals( Double.valueOf(11.5),lens.get.apply(c));


    }

    @Test
    public void testLongLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     Long.MAX_VALUE
                                    )
                          );

        JsLongLens<JsObj> lens = JsObj.optics.lens.longNum(path);

        Assertions.assertEquals( Long.valueOf(Long.MAX_VALUE),lens.get.apply(a));


        JsObj b = lens.set.apply(Long.MIN_VALUE)
                          .apply(a);

        Assertions.assertEquals( Long.valueOf(Long.MIN_VALUE),lens.get.apply(b));

        JsObj c = lens.modify.apply(i->i+1)
                             .apply(b);

        Assertions.assertEquals( Long.valueOf(Long.MIN_VALUE+1),lens.get.apply(c));



    }

    @Test
    public void testIntegerLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                     Integer.MAX_VALUE
                                    )
                          );

        JsIntLens<JsObj> lens = JsObj.optics.lens.intNum(path);

        Assertions.assertEquals( Integer.valueOf(Integer.MAX_VALUE),lens.get.apply(a));


        JsObj b = lens.set.apply(Integer.MIN_VALUE)
                          .apply(a);

        Assertions.assertEquals( Integer.valueOf(Integer.MIN_VALUE),lens.get.apply(b));

        JsObj c = lens.modify.apply(i->i+1)
                             .apply(b);

        Assertions.assertEquals( Integer.valueOf(Integer.MIN_VALUE+1),lens.get.apply(c));



    }

    @Test
    public void testDecimalLenses() {

        JsPath path = JsPath.path("/a/b/c");
        JsObj a = JsObj.of(JsPair.of(path,
                                    JsBigDec.of(new BigDecimal("1.11"))
                                    )
                          );

        JsDecimalLens<JsObj> lens = JsObj.optics.lens.decimalNum(path);

        Assertions.assertEquals(new BigDecimal("1.11") ,lens.get.apply(a));


        JsObj b = lens.set.apply(new BigDecimal("10.11"))
                          .apply(a);

        Assertions.assertEquals(new BigDecimal("10.11"),lens.get.apply(b));

        JsObj c = lens.modify.apply(i->i.plus().add(BigDecimal.valueOf(10.0)))
                             .apply(b);

        Assertions.assertEquals( new BigDecimal("20.11"),lens.get.apply(c));



    }
}
