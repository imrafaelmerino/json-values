package jsonvalues.optics;

import fun.optic.Lens;
import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


public class TestLenses {


    @Test
    public void testCompose() {

        Lens<JsObj, JsObj> address = JsObj.lens.obj("address");

        Lens<JsObj, String> street = JsObj.lens.str("street");

        Lens<JsObj, String> compose = address.compose(street);

        JsObj obj = JsObj.of("address",
                             JsObj.of("street",
                                      JsStr.of("a")
                             )
        );

        Assertions.assertEquals(obj,
                                compose.set.apply("a")
                                           .apply(JsObj.empty()));

        Assertions.assertEquals("a",
                                compose.get.apply(obj)
        );

        Assertions.assertEquals("b",
                                compose.get.apply(compose.set.apply("b")
                                                             .apply(obj))
        );
    }

    @Test
    public void testGet() {


        Lens<JsObj, Boolean> bool = JsObj.lens.bool("bool");

        Lens<JsObj, Long> longnum = JsObj.lens.longNum("long");

        Lens<JsObj, Double> doublenum = JsObj.lens.doubleNum("double");

        Lens<JsObj, BigInteger> bigintNum = JsObj.lens.integralNum("bigint");


        Lens<JsObj, byte[]> bytes = JsObj.lens.binary("binary");


        JsObj obj = JsObj.of(
                "bool",
                JsBool.TRUE,
                "long",
                JsLong.of(10),
                "double",
                JsDouble.of(10.5d),
                "bigint",
                JsBigInt.of(new BigInteger("10000000")),
                "binary",
                JsBinary.of("hola".getBytes(StandardCharsets.UTF_8))
        );

        Assertions.assertEquals(new BigInteger("10000000"),
                                bigintNum.get.apply(obj));

        Assertions.assertArrayEquals("hola".getBytes(StandardCharsets.UTF_8),bytes.get.apply(obj));

        Assertions.assertTrue(bool.get.apply(obj));

        Assertions.assertEquals(10,
                                longnum.get.apply(obj));

        Assertions.assertEquals(10.5,
                                doublenum.get.apply(obj));


    }

    @Test
    public void test_binary_lens() {

        Lens<JsObj, byte[]> binaryLens = JsObj.lens.binary("a");


        JsObj obj = JsObj.of("a",
                             JsStr.of("hola"));

        byte[] bytes = binaryLens.get.apply(obj);

        Assertions.assertArrayEquals(bytes,
                                     JsStr.base64Prism.getOptional.apply("hola")
                                                                  .get()
        );


        JsObj a = JsObj.of("a",
                           JsBinary.of("hola".getBytes(StandardCharsets.UTF_8)));
        JsObj newObj = binaryLens.modify.apply(it -> (new String(it) + "adios")
                .getBytes(StandardCharsets.UTF_8)).apply(a);
        Assertions.assertArrayEquals(binaryLens.get.apply(newObj),
                                     "holaadios".getBytes(StandardCharsets.UTF_8));

    }

    @Test
    public void test_instant_lens() {

        Lens<JsObj, Instant> intantLens = JsObj.lens.instant("a");


        Instant now = Instant.now();
        Instant instant = intantLens.get.apply(JsObj.of("a",
                                                        JsStr.of(now.toString())
        ));

        Assertions.assertEquals(instant,
                                JsStr.instantPrism.getOptional.apply(now.toString())
                                                              .get()
        );

        JsObj a = intantLens.set.apply(now).apply(JsObj.empty());
        JsObj b = intantLens.modify.apply(i -> i.plusSeconds(100)).apply(a);
        Assertions.assertEquals(instant.plusSeconds(100),
                                intantLens.get.apply(b)
        );

        JsObj c = JsObj.empty().set(JsPath.path("/a/0/b"),
                                    JsInstant.of(now));

        Lens<JsObj, Instant> lens = JsObj.lens.instant(JsPath.path("/a/0/b"));

        Assertions.assertEquals(now,
                                lens.get.apply(c));

    }
}
