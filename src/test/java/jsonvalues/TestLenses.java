package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLenses {


    @Test
    public void testCompose(){

        Lens<JsObj,JsObj> address =  JsObj.lens.obj("address");

        Lens<JsObj,String> street = JsObj.lens.str("street");

        Lens<JsObj, String> compose = address.compose(street);

        JsObj obj = JsObj.of("address",
                            JsObj.of("street",
                                     JsStr.of("a")));

        Assertions.assertEquals("a",compose.get.apply(obj));

        Assertions.assertEquals("b",compose.get.apply(compose.set.apply("b").apply(obj)));
    }
}
