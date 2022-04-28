package jsonvalues;


import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;


public class Functions {

    private Functions() {
    }

    public static void assertErrorIs(final Set<JsErrorPair> error,
                                     final ERROR_CODE code,
                                     final JsValue value,
                                     final JsPath path
    ) {
        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                code
        );

        Assertions.assertEquals(pair.error.value,
                                value
        );

        Assertions.assertEquals(pair.path,
                                path
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
