package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Assertions.assertEquals(obj,compose.set.apply("a")
                                   .apply(JsObj.empty()));

        Assertions.assertEquals("a",
                                compose.get.apply(obj));

        Assertions.assertEquals("b",
                                compose.get.apply(compose.set.apply("b")
                                                             .apply(obj)));
    }

    @Test
    public void test_binary_lens() {

        Lens<JsObj, byte[]> binaryLens = JsObj.lens.binary("a");


        byte[] bytes = binaryLens.get.apply(JsObj.of("a",
                                                     JsStr.of("hola")
                                                    ));

        Assertions.assertArrayEquals(bytes,
                                     JsStr.base64Prism.getOptional.apply("hola")
                                                                  .get()
                                    );


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
    }
}
