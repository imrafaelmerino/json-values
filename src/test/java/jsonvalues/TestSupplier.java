package jsonvalues;

import jsonvalues.supplier.JsObjSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSupplier {


    @Test
    public void testSupplier() {


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
}
