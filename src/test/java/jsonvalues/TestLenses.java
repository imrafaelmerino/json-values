package jsonvalues;

import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

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

    @Test
    public void test() {

        JsObj response = null;

        JsObjSpec responseSpec = JsObjSpec.strict("items",
                                                  JsSpecs.arrayOf(JsObjSpec.strict("response",
                                                                                   JsSpecs.str(s->true),
                                                                                   "status",
                                                                                   JsSpecs.str(s->true))
                                                  )
        );


        Lens<JsObj, String> statusLens = JsObj.lens.str("status");
        Lens<JsObj, String> responseLens = JsObj.lens.str("status");
        Lens<JsObj, JsArray> itemsLens = JsObj.lens.array("items");


        Function<JsObj, Optional<String>> statusIsNotPending = statusLens.find.apply(s -> !s.equals("PENDING"));
        Function<JsObj, Optional<String>> responseIsNotNoRequested = responseLens.find.apply(s -> !s.equals("NO_REQUESTED"));




        Assertions.assertTrue(responseSpec.test(response).isEmpty(),
                              "Respuesta no cumple spec");

        boolean statusIsNotPendingAssertion =
                itemsLens.find.apply(consents -> consents.filterAllObjs(consent -> statusIsNotPending.apply(consent).isPresent())
                                                         .isNotEmpty())
                              .apply(response)
                              .isPresent();
        Assertions.assertTrue(statusIsNotPendingAssertion,
                              "status is PENDING");

        Assertions.assertTrue(itemsLens.find.apply(consents -> consents.filterAllObjs(consent -> responseIsNotNoRequested.apply(consent).isPresent()).isNotEmpty()).apply(response).isPresent(),
                              "response is NOT_REQUESTED");


    }

}
