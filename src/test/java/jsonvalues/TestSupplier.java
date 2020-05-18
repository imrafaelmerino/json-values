package jsonvalues;

import jsonvalues.supplier.JsArraySupplier;
import jsonvalues.supplier.JsObjSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSupplier {


    @Test
    public void testSupplier() {


        JsObjSupplier supplier = JsObjSupplier.of("a",
                                                  () -> JsInt.of(1),
                                                  "b",
                                                  () -> JsBool.TRUE,
                                                  "c", JsObjSupplier.of("d",()->JsNull.NULL),
                                                  "e",
                                                  JsArraySupplier.of(()-> JsDouble.of(1.5),()->JsBool.FALSE)
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
}
